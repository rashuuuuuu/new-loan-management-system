package com.rashmita.prepaymentservice.service.Impl;
import com.rashmita.commoncommon.entity.*;
import com.rashmita.commoncommon.model.*;
import com.rashmita.commoncommon.repository.*;
import com.rashmita.prepaymentservice.client.BankClient;
import com.rashmita.prepaymentservice.client.IsoClient;
import com.rashmita.prepaymentservice.service.PrepaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PrepaymentServiceImpl implements PrepaymentService {
    private final BankClient bankClient;
    private final LoanDetailsRepository loanDetailsRepository;
    private final ModelMapper modelMapper;
    private final TotalPayableRepository totalPayableRepository;
    private final PrepaymentLogRepository prepaymentLogRepository;
    private final EmiInterestRepository emiInterestRepository;
    private final PrepaymentInquiryRepository prepaymentInquiryRepository;
    private final PrepaymentDetailsRepository prepaymentDetailsRepository;
    private final IsoClient isoClient;

    public PrepaymentServiceImpl(BankClient bankClient, LoanDetailsRepository loanDetailsRepository, PrepaymentInquiryRepository prepaymentRepository, ModelMapper modelMapper, TotalPayableRepository totalPayableRepository, PrepaymentLogRepository prepaymentLogRepository, EmiInterestRepository emiInterestRepository, PrepaymentInquiryRepository prepaymentInquiryRepository, PrepaymentDetailsRepository prepaymentDetailsRepository, IsoClient isoClient) {
        this.bankClient = bankClient;
        this.loanDetailsRepository = loanDetailsRepository;
        this.prepaymentDetailsRepository = prepaymentDetailsRepository;
        this.modelMapper = modelMapper;
        this.totalPayableRepository = totalPayableRepository;
        this.prepaymentLogRepository = prepaymentLogRepository;
        this.emiInterestRepository = emiInterestRepository;
        this.prepaymentInquiryRepository = prepaymentInquiryRepository;
        this.isoClient = isoClient;
    }

    @Override
    public PrepaymentInquiryResponseModel prepaymentInquiry(PrepaymentInquiryRequestModel prepaymentInquiryRequestModel) {
        PrepaymentInquiry prepaymentInquiry = prepaymentInquiryRepository.findByLoanNumber(prepaymentInquiryRequestModel.getLoanNumber()).orElse(new PrepaymentInquiry());
        Optional<LoanDetails> loanDetails = loanDetailsRepository.findByLoanNumber(prepaymentInquiryRequestModel.getLoanNumber());
        String bankCode = loanDetails.get().getBankCode();
        LoanConfigBankCodeRequest loanConfigBankCodeRequest = new LoanConfigBankCodeRequest();
        loanConfigBankCodeRequest.setBankCode(bankCode);
        Double remainingPrincipal = totalRemainingPrincipal(prepaymentInquiryRequestModel.getLoanNumber());
        LoanConfigurationResponse loanConfigurationResponse = bankClient.getLoanConfigurationByBankCode(loanConfigBankCodeRequest).getData();
        int prepaymentPercentage = loanConfigurationResponse.getPrepaymentPercentage();
        Double prepaymentChargeAmount = (prepaymentPercentage * remainingPrincipal) / 100;
        int prepaymentFlat = loanConfigurationResponse.getPrepaymentFlat();
        prepaymentInquiry.setLoanNumber(prepaymentInquiryRequestModel.getLoanNumber());
        prepaymentInquiry.setPrepaymentDate(prepaymentInquiryRequestModel.getPrepaymentDate());
        prepaymentInquiry.setPrePaymentCharge(max(prepaymentChargeAmount, prepaymentFlat));
        int interestAccruedDays = totalDaysInterestAccruedTillPrepayment(prepaymentInquiryRequestModel.getLoanNumber(), prepaymentInquiryRequestModel.getPrepaymentDate());
        int interestRate = loanConfigurationResponse.getInterestRate();
        Double interestToPay = interestAccruedDays * ((remainingPrincipal * interestRate) / (100 * 365));
        prepaymentInquiry.setPayableAmount(remainingPrincipal + interestToPay + prepaymentChargeAmount);
        PrepaymentLog prepaymentLog = new PrepaymentLog();
        prepaymentLog.setLoanNumber(prepaymentInquiryRequestModel.getLoanNumber());
        prepaymentLog.setType("Inquiry");
        prepaymentLog.setStatus("Success");
        prepaymentLog.setAmount(prepaymentInquiry.getPayableAmount());
        prepaymentLogRepository.save(prepaymentLog);
        prepaymentInquiryRepository.save(prepaymentInquiry);
        return modelMapper.map(prepaymentInquiry, PrepaymentInquiryResponseModel.class);
    }
    @Override
    public PrepaymentResponse createPrepayment(PrepaymentRequest prepaymentRequest) {
        Optional<LoanDetails> loanDetailsOpt = loanDetailsRepository.findByLoanNumber(prepaymentRequest.getLoanNumber());
        if (loanDetailsOpt.isEmpty()) {
            ResponseUtility.getFailedServerResponse("Loan not found for loan number: " + prepaymentRequest.getLoanNumber());
        }
        LoanDetails loanDetails = loanDetailsOpt.get();
        String customerNumber = loanDetails.getCustomerNumber();
        String bankCode = loanDetails.getBankCode();
        BankIdAndCustomerRequest bankIdAndCustomerRequest = new BankIdAndCustomerRequest();
        bankIdAndCustomerRequest.setBankCode(bankCode);
        bankIdAndCustomerRequest.setCustomerNumber(customerNumber);
        CustomerResponse customerResponse = bankClient.getCustomerByBankCodeAndCustomerNumber(bankIdAndCustomerRequest);
        Double amountOnAccount = customerResponse.getAmount() == null ? 0.0 : customerResponse.getAmount();
        LocalDate prepaymentDate = prepaymentRequest.getPrepaymentDate();
        Optional<PrepaymentInquiry> prepaymentInquiryOpt = prepaymentInquiryRepository.findByLoanNumber(prepaymentRequest.getLoanNumber());
        if (!amountOnAccount.equals(prepaymentRequest.getPaymentAmount())) {
            ResponseUtility.getFailedServerResponse("you don't have enough balance for prepayment");
        }
        if (prepaymentInquiryOpt.isEmpty()) {
            ResponseUtility.getFailedServerResponse("No prepayment inquiry found for loan number: " + prepaymentRequest.getLoanNumber());
        }
        PrepaymentInquiry prepaymentInquiry = prepaymentInquiryOpt.get();
        if (!prepaymentInquiry.getPayableAmount().equals(prepaymentRequest.getPaymentAmount())) {
            ResponseUtility.getFailedServerResponse("Inquiry balance and apply balance must match for prepayment");
        }
        PrepaymentDetails prepaymentDetails = new PrepaymentDetails();
        prepaymentDetails.setPrepaymentDate(prepaymentDate);
        prepaymentDetails.setLoanNumber(prepaymentRequest.getLoanNumber());
        prepaymentDetails.setPaidAmount(prepaymentInquiry.getPayableAmount());
        prepaymentDetails.setPrePaymentCharge(prepaymentInquiry.getPrePaymentCharge());
        loanDetails.setStatus("SETTLED");
        PrepaymentLog prepaymentLog = new PrepaymentLog();
        prepaymentLog.setLoanNumber(prepaymentRequest.getLoanNumber());
        prepaymentLog.setType("prepayment");
        List<TotalPayableEntity> totalPayableEntity = totalPayableRepository.findByLoanNumber(prepaymentRequest.getLoanNumber());
        List<TotalPayableEntity> totalPayableEntities=new ArrayList<>();
        for (TotalPayableEntity totalPayableEntity1 : totalPayableEntity) {
            totalPayableEntity1.setPayablePrincipal(0.0);
            totalPayableEntity1.setPayableInterest(0.0);
            totalPayableEntity1.setPayablePenalty(0.0);
            totalPayableEntity1.setPayableOverdue(0.0);
            totalPayableEntity1.setPayableLateFee(0.0);
            totalPayableEntity1.setTotalPayable(0.0);
            totalPayableEntity1.setStatus("paid");
            totalPayableEntities.add(totalPayableEntity1);
        }
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setLoanNumber(prepaymentRequest.getLoanNumber());
        transactionRequest.setTransactionId(loanDetails.getTransaction_token());
        TransactionDetailRequest transaction = new TransactionDetailRequest(
                loanDetails.getAccountNumber(),
                "Debit",
                prepaymentRequest.getPaymentAmount(),
                "Prepayment",
                LocalDate.now()
        );
        transactionRequest.setTransactions(Collections.singletonList(transaction));
        isoClient.isoPrepayment(transactionRequest);
        totalPayableRepository.saveAll(totalPayableEntities);
        prepaymentLogRepository.save(prepaymentLog);
        loanDetailsRepository.save(loanDetails);
        prepaymentDetailsRepository.save(prepaymentDetails);
        return modelMapper.map(prepaymentDetails, PrepaymentResponse.class);
    }

    private Double max(Double prepaymentChargeAmount, int prepaymentFlat) {
        return prepaymentChargeAmount > prepaymentFlat ? prepaymentChargeAmount : prepaymentFlat;
    }

    private Double totalRemainingPrincipal(String loanNumber) {
        List<TotalPayableEntity> totalPayableEntities = totalPayableRepository.findByLoanNumber(loanNumber);
        Double remainingPrincipal = 0.0;
        for (TotalPayableEntity totalPayableEntity : totalPayableEntities) {
            if (totalPayableEntity.getStatus().equals("PAID")) {
                remainingPrincipal = 0.0;
            } else if (totalPayableEntity.getStatus().equals("UNPAID")) {
                remainingPrincipal += totalPayableEntity.getPayablePrincipal();
            }
        }
        return remainingPrincipal;
    }

    private int totalDaysInterestAccruedTillPrepayment(String loanNumber, LocalDate prepaymentDate) {
        List<TotalPayableEntity> totalPayableEntities = totalPayableRepository.findByLoanNumber(loanNumber);
        int days = 0;
        for (TotalPayableEntity totalPayableEntity : totalPayableEntities) {
            if (totalPayableEntity.getStatus().equals("UNPAID")) {
                LocalDate emiDate = totalPayableEntity.getEmiDate();
                LocalDate paymentDate = prepaymentDate;
                days = (emiDate.getDayOfMonth() - paymentDate.getDayOfMonth());
            }
        }
        return days;
    }

    private Double totalInterestAccrued(String loanNumber) {
        List<TotalPayableEntity> totalPayableEntities = totalPayableRepository.findByLoanNumber(loanNumber);
        Double interest = 0.0;
        for (TotalPayableEntity totalPayableEntity : totalPayableEntities) {
            if (totalPayableEntity.getStatus().equals("PAID")) {
                interest = 0.0;
            } else if (totalPayableEntity.getStatus().equals("UNPAID")) {
                interest += totalPayableEntity.getPayableInterest();
            }
        }
        return interest;
    }
}
