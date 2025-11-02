package com.rashmita.settlementservice.scheduler;

import com.rashmita.commoncommon.entity.LoanDetails;
import com.rashmita.commoncommon.entity.TotalPayableEntity;
import com.rashmita.commoncommon.model.BankIdAndCustomerRequest;
import com.rashmita.commoncommon.model.CustomerResponse;
import com.rashmita.commoncommon.model.SettlementRequest;
import com.rashmita.commoncommon.model.TransactionDetailRequest;
import com.rashmita.commoncommon.repository.EmiScheduleRepository;
import com.rashmita.commoncommon.repository.LoanDetailsRepository;
import com.rashmita.commoncommon.repository.PaymentDetailsRepository;
import com.rashmita.commoncommon.repository.TotalPayableRepository;
import com.rashmita.settlementservice.client.BankClient;
import com.rashmita.settlementservice.client.IsoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class SettlementScheduler {
    private final PaymentDetailsRepository paymentDetailsRepository;
    private final EmiScheduleRepository emiScheduleRepository;
    private final LoanDetailsRepository loanDetailsRepository;
    private final TotalPayableRepository totalPayableRepository;
    private final BankClient bankClient;
    private final IsoClient isoClient;

    public SettlementScheduler(PaymentDetailsRepository paymentDetailsRepository, EmiScheduleRepository emiScheduleRepository,
                               LoanDetailsRepository loanDetailsRepository, TotalPayableRepository totalPayableRepository,
                               BankClient bankClient, IsoClient isoClient) {
        this.paymentDetailsRepository = paymentDetailsRepository;
        this.emiScheduleRepository = emiScheduleRepository;
        this.loanDetailsRepository = loanDetailsRepository;
        this.totalPayableRepository = totalPayableRepository;
        this.bankClient = bankClient;
        this.isoClient = isoClient;
    }

    @Scheduled(fixedRate = 60000)
    public void run() {
        List<LoanDetails> loanDetails = loanDetailsRepository.findAll();
        for (LoanDetails loanDetail : loanDetails) {
            List<TotalPayableEntity> totalPayableEntities = totalPayableRepository.findByLoanNumber(loanDetail.getLoanNumber());
            String customerNumber = loanDetail.getCustomerNumber();
            String bankCode = loanDetail.getBankCode();

            BankIdAndCustomerRequest bankIdAndCustomerRequest = new BankIdAndCustomerRequest();
            bankIdAndCustomerRequest.setBankCode(bankCode);
            bankIdAndCustomerRequest.setCustomerNumber(customerNumber);

            CustomerResponse customerResponse = bankClient.getCustomerByBankCodeAndCustomerNumber(bankIdAndCustomerRequest);
            Double amountOnAccount = customerResponse.getAmount() == null ? 0.0 : customerResponse.getAmount();

            for (TotalPayableEntity totalPayableEntity : totalPayableEntities) {
                LocalDate emiDate = totalPayableEntity.getEmiDate();
                if (emiDate.isAfter(LocalDate.now())) continue;

                while (emiDate.isBefore(LocalDate.now())) {
                    TransactionDetailRequest transactionDetailRequest = new TransactionDetailRequest(
                            loanDetail.getAccountNumber(),
                            "Debit",
                            totalPayableEntity.getTotalPayable(),
                            "loan settle for " + totalPayableEntity.getEmiDate(),
                            LocalDate.now()
                    );
                    SettlementRequest settlementRequest = new SettlementRequest();
                    settlementRequest.setTransactions(Collections.singletonList(transactionDetailRequest));
                    settlementRequest.setEmiMonth(totalPayableEntity.getTenure());
                    settlementRequest.setAmount(totalPayableEntity.getTotalPayable());
                    settlementRequest.setLoanNumber(loanDetail.getLoanNumber());
                    settlementRequest.setAccountNumber(loanDetail.getAccountNumber());
                    isoClient.isoSettlement(settlementRequest);

                    if (Math.abs(amountOnAccount - totalPayableEntity.getTotalPayable()) < 0.01) {
                        totalPayableEntity.setPaidInterest(defaultZero(totalPayableEntity.getPaidInterest()) + totalPayableEntity.getPayableInterest());
                        totalPayableEntity.setPaidOverdue(defaultZero(totalPayableEntity.getPaidOverdue()) + totalPayableEntity.getPayableOverdue());
                        totalPayableEntity.setPaidLateFee(defaultZero(totalPayableEntity.getPaidLateFee()) + totalPayableEntity.getPayableLateFee());
                        totalPayableEntity.setPaidPenalty(defaultZero(totalPayableEntity.getPaidPenalty()) + totalPayableEntity.getPayablePenalty());
                        totalPayableEntity.setPaidPrincipal(defaultZero(totalPayableEntity.getPaidPrincipal()) + totalPayableEntity.getPayablePrincipal());
                        totalPayableEntity.setPayablePenalty(0.0);
                        totalPayableEntity.setPayableInterest(0.0);
                        totalPayableEntity.setPayableOverdue(0.0);
                        totalPayableEntity.setPayableLateFee(0.0);
                        totalPayableEntity.setPayablePrincipal(0.0);
                        totalPayableEntity.setTotalPayable(0.0);
                        totalPayableRepository.save(totalPayableEntity);
                    } else {
                        while (amountOnAccount < totalPayableEntity.getTotalPayable()) {
                            double remaining = amountOnAccount;
                            remaining = applyPayment(remaining, totalPayableEntity, "lateFee");
                            remaining = applyPayment(remaining, totalPayableEntity, "penalty");
                            remaining = applyPayment(remaining, totalPayableEntity, "overdue");
                            remaining = applyPayment(remaining, totalPayableEntity, "interest");
                            remaining = applyPayment(remaining, totalPayableEntity, "principal");
                            if (totalPayableEntity.getTotalPayable() < 0) {
                                totalPayableEntity.setTotalPayable(0.0);
                            }

                            // When balance exhausted, adjust remaining payable
                            if (remaining <= 0) {
                                double newTotal = totalPayableEntity.getPayablePenalty() +
                                        totalPayableEntity.getPayableLateFee() +
                                        totalPayableEntity.getPayableOverdue() +
                                        totalPayableEntity.getPayableInterest() +
                                        totalPayableEntity.getPayablePrincipal();
                                totalPayableEntity.setTotalPayable(Math.max(newTotal, 0.0));
                                totalPayableRepository.save(totalPayableEntity);
                                break;
                            }

                            amountOnAccount = remaining;
                        }
                    }

                    emiDate = emiDate.plusMonths(1); // avoid infinite loop
                }
            }
        }
    }

    private double applyPayment(double remaining, TotalPayableEntity payable, String type) {
        switch (type) {
            case "lateFee":
                double lateFee = Math.min(remaining, defaultZero(payable.getPayableLateFee()));
                payable.setPaidLateFee(defaultZero(payable.getPaidLateFee()) + lateFee);
                payable.setPayableLateFee(payable.getPayableLateFee() - lateFee);
                remaining -= lateFee;
                break;

            case "penalty":
                double penalty = Math.min(remaining, defaultZero(payable.getPayablePenalty()));
                payable.setPaidPenalty(defaultZero(payable.getPaidPenalty()) + penalty);
                payable.setPayablePenalty(payable.getPayablePenalty() - penalty);
                remaining -= penalty;
                break;

            case "overdue":
                double overdue = Math.min(remaining, defaultZero(payable.getPayableOverdue()));
                payable.setPaidOverdue(defaultZero(payable.getPaidOverdue()) + overdue);
                payable.setPayableOverdue(payable.getPayableOverdue() - overdue);
                remaining -= overdue;
                break;

            case "interest":
                double interestAmount = Math.min(remaining, defaultZero(payable.getPayableInterest()));
                payable.setPaidInterest(defaultZero(payable.getPaidInterest()) + interestAmount);
                payable.setPayableInterest(payable.getPayableInterest() - interestAmount);
                remaining -= interestAmount;
                break;

            case "principal":
                double principal = Math.min(remaining, defaultZero(payable.getPayablePrincipal()));
                payable.setPaidPrincipal(defaultZero(payable.getPaidPrincipal()) + principal);
                payable.setPayablePrincipal(payable.getPayablePrincipal() - principal);
                double recalculatedTotal = defaultZero(payable.getPayablePenalty())
                        + defaultZero(payable.getPayableLateFee())
                        + defaultZero(payable.getPayableOverdue())
                        + defaultZero(payable.getPayableInterest())
                        + defaultZero(payable.getPayablePrincipal());
                payable.setTotalPayable(Math.max(recalculatedTotal, 0.0));

                remaining -= principal;
                break;
        }
        return remaining;
    }
    private double defaultZero(Double val) {
        return val == null ? 0.0 : val;
    }
}
