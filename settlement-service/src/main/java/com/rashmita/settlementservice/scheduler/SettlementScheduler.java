package com.rashmita.settlementservice.scheduler;

import com.rashmita.commoncommon.entity.LoanDetails;
import com.rashmita.commoncommon.entity.TotalPayableEntity;
import com.rashmita.commoncommon.model.BankIdAndCustomerRequest;
import com.rashmita.commoncommon.model.CustomerResponse;
import com.rashmita.commoncommon.model.SettlementRequest;
import com.rashmita.commoncommon.model.TransactionDetailRequest;
import com.rashmita.commoncommon.repository.*;
import com.rashmita.settlementservice.client.BankClient;
import com.rashmita.settlementservice.client.IsoClient;
import com.rashmita.settlementservice.service.Impl.SettlementServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SettlementScheduler {

    private final PaymentDetailsRepository paymentDetailsRepository;
    private final EmiScheduleRepository emiScheduleRepository;
    private final LoanDetailsRepository loanDetailsRepository;
    private final TotalPayableRepository totalPayableRepository;
    private final BankClient bankClient;
    private final IsoClient isoClient;
    private final SettlementServiceImpl settlementService;

    public SettlementScheduler(PaymentDetailsRepository paymentDetailsRepository,
                               EmiScheduleRepository emiScheduleRepository,
                               LoanDetailsRepository loanDetailsRepository,
                               TotalPayableRepository totalPayableRepository,
                               BankClient bankClient,
                               IsoClient isoClient,
                               SettlementServiceImpl settlementService) {
        this.paymentDetailsRepository = paymentDetailsRepository;
        this.emiScheduleRepository = emiScheduleRepository;
        this.loanDetailsRepository = loanDetailsRepository;
        this.totalPayableRepository = totalPayableRepository;
        this.bankClient = bankClient;
        this.isoClient = isoClient;
        this.settlementService = settlementService;
    }
    @Scheduled(fixedRate = 60000)
    public void run() {
        List<LoanDetails> loanDetailsList = loanDetailsRepository.findAll();

        for (LoanDetails loanDetail : loanDetailsList) {
            List<TotalPayableEntity> totalPayableEntities =
                    totalPayableRepository.findByLoanNumber(loanDetail.getLoanNumber());
            if (totalPayableEntities.isEmpty()) continue;

            BankIdAndCustomerRequest request = new BankIdAndCustomerRequest();
            request.setBankCode(loanDetail.getBankCode());
            request.setCustomerNumber(loanDetail.getCustomerNumber());
            CustomerResponse customerResponse = bankClient.getCustomerByBankCodeAndCustomerNumber(request);
            double amountOnAccount = defaultZero(customerResponse.getAmount());
            List<TotalPayableEntity> unpaidEmis = totalPayableEntities.stream()
                    .filter(e -> "UNPAID".equalsIgnoreCase(e.getStatus())
                            && e.getEmiDate() != null
                            && !e.getEmiDate().isAfter(LocalDate.now()))
                    .sorted(Comparator.comparing(TotalPayableEntity::getEmiDate))
                    .collect(Collectors.toList());

            if (unpaidEmis.isEmpty()) continue;

            log.info("Processing {} unpaid EMIs for loan {}", unpaidEmis.size(), loanDetail.getLoanNumber());
            double remainingBalance = applyKnockOffPriority(amountOnAccount, unpaidEmis);
            for (TotalPayableEntity e : unpaidEmis) {
                double newTotal = defaultZero(e.getPayableLateFee())
                        + defaultZero(e.getPayablePenalty())
                        + defaultZero(e.getPayableOverdue())
                        + defaultZero(e.getPayableInterest())
                        + defaultZero(e.getPayablePrincipal());
                e.setTotalPayable(newTotal);
                if (newTotal == 0.0) e.setStatus("PAID");
                totalPayableRepository.save(e);
            }

            if (remainingBalance < amountOnAccount) {
                double settledAmount = amountOnAccount - remainingBalance;
                TransactionDetailRequest transactionDetail = new TransactionDetailRequest(
                        loanDetail.getAccountNumber(),
                        "Debit",
                        settledAmount,
                        "Knock-off priority settlement for " + loanDetail.getLoanNumber(),
                        LocalDate.now()
                );

                SettlementRequest settlementRequest = new SettlementRequest();
                settlementRequest.setTransactions(Collections.singletonList(transactionDetail));
                settlementRequest.setAmount(settledAmount);
                settlementRequest.setLoanNumber(loanDetail.getLoanNumber());
                settlementRequest.setAccountNumber(loanDetail.getAccountNumber());

                isoClient.isoSettlement(settlementRequest);
            }
        }

        log.info("Settlement Scheduler executed successfully with global knock-off priority.");
    }
    private double applyKnockOffPriority(double balance, List<TotalPayableEntity> unpaidEmis) {
        balance = settleType(balance, unpaidEmis, "lateFee");
        balance = settleType(balance, unpaidEmis, "penalty");
        balance = settleType(balance, unpaidEmis, "overdue");
        balance = settleType(balance, unpaidEmis, "interest");
        balance = settleType(balance, unpaidEmis, "principal");
        return balance;
    }
    private double settleType(double remaining, List<TotalPayableEntity> emis, String type) {
        for (TotalPayableEntity e : emis) {
            if (remaining <= 0) break;
            remaining = applyPayment(remaining, e, type);
        }
        return remaining;
    }
    private double applyPayment(double remaining, TotalPayableEntity payable, String type) {
        double payableAmt;
        double paidAmt;

        switch (type) {
            case "lateFee":
                payableAmt = defaultZero(payable.getPayableLateFee());
                paidAmt = Math.min(remaining, payableAmt);
                payable.setPaidLateFee(paidAmt);
                payable.setPayableLateFee(payableAmt - paidAmt);
                remaining -= paidAmt;
                break;
            case "penalty":
                payableAmt = defaultZero(payable.getPayablePenalty());
                paidAmt = Math.min(remaining, payableAmt);
                payable.setPaidPenalty( paidAmt);
                payable.setPayablePenalty(payableAmt - paidAmt);
                remaining -= paidAmt;
                break;
            case "overdue":
                payableAmt = defaultZero(payable.getPayableOverdue());
                paidAmt = Math.min(remaining, payableAmt);
                payable.setPaidOverdue( paidAmt);
                payable.setPayableOverdue(payableAmt - paidAmt);
                remaining -= paidAmt;
                break;
            case "interest":
                payableAmt = defaultZero(payable.getPayableInterest());
                paidAmt = Math.min(remaining, payableAmt);
                payable.setPaidInterest(paidAmt);
                payable.setPayableInterest(payableAmt - paidAmt);
                remaining -= paidAmt;
                break;
            case "principal":
                payableAmt = defaultZero(payable.getPayablePrincipal());
                paidAmt = Math.min(remaining, payableAmt);
                payable.setPaidPrincipal(paidAmt);
                payable.setPayablePrincipal(payableAmt - paidAmt);
                remaining -= paidAmt;
                break;
        }
        return Math.max(remaining, 0);
    }

    private double defaultZero(Double val) {
        return val == null ? 0.0 : val;
    }
}
