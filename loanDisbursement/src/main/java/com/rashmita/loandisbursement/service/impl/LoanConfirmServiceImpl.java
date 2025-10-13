package com.rashmita.loandisbursement.service.impl;

import com.rashmita.commoncommon.model.ResponseUtility;
import com.rashmita.commoncommon.model.ServerResponse;
import com.rashmita.commoncommon.model.TransactionDetailRequest;
import com.rashmita.commoncommon.model.TransactionRequest;
import com.rashmita.loandisbursement.client.IsoClient;
import com.rashmita.loandisbursement.entity.EmiSchedule;
import com.rashmita.loandisbursement.entity.LoanDetails;
import com.rashmita.loandisbursement.model.LoanBookResponse;
import com.rashmita.loandisbursement.model.LoanConfirmRequest;
import com.rashmita.loandisbursement.model.LoanConfirmResponse;
import com.rashmita.loandisbursement.repository.EmiScheduleRepository;
import com.rashmita.loandisbursement.repository.LoanDetailsRepository;
import com.rashmita.loandisbursement.service.LoanConfirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoanConfirmServiceImpl implements LoanConfirmService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final LoanDetailsRepository loanDetailsRepository;
    private final EmiScheduleRepository emiScheduleRepository;
    private final IsoClient isoClient;

    public LoanConfirmServiceImpl(RedisTemplate<String, Object> redisTemplate,
                                  LoanDetailsRepository loanDetailsRepository,
                                  EmiScheduleRepository emiScheduleRepository, IsoClient isoClient) {
        this.redisTemplate = redisTemplate;
        this.loanDetailsRepository = loanDetailsRepository;
        this.emiScheduleRepository = emiScheduleRepository;
        this.isoClient = isoClient;
    }

    @Override
    public ServerResponse<?> confirmLoan(LoanConfirmRequest request) {
        LoanDetails loanDetail = new LoanDetails();
        String cacheKey = "loanBook:" + request.getAccountNumber();
        LoanBookResponse loanBookResponse = (LoanBookResponse) redisTemplate.opsForValue().get(cacheKey);

        if (loanBookResponse == null) {
            return ResponseUtility.getFailedServerResponse(
                    "Loan booking not found or expired. Please complete loan booking first."
            );
        }

        if (!"Booked".equalsIgnoreCase(loanBookResponse.getStatus())) {
            return ResponseUtility.getFailedServerResponse(
                    "Loan cannot be confirmed. Current status: " + loanBookResponse.getStatus()
            );
        }

        if (!loanBookResponse.getOtp().equals(request.getOtp())) {
            return ResponseUtility.getFailedServerResponse("Invalid OTP.");
        }

        String loanNumber = "LN" + UUID.randomUUID().toString().replace("-", "").substring(0, 18).toUpperCase();

        loanDetail.setCode(UUID.randomUUID().toString());
        loanDetail.setBankCode(request.getBankCode());
        loanDetail.setLoanNumber(loanNumber);
        loanDetail.setTransaction_token(loanBookResponse.getTransactionToken());
        loanDetail.setCustomerNumber(request.getCustomerNumber());
        loanDetail.setAccountNumber(loanBookResponse.getAccountNumber());
        loanDetail.setLoanAmount(Double.parseDouble(request.getEmiAmount()));
        loanDetail.setTenure(Integer.parseInt(request.getEmiMonths()));
        loanDetail.setStatus("Confirmed");
        loanDetail.setCreatedAt(new Date());
        loanDetail.setStartDate(java.sql.Date.valueOf(request.getPaymentDate()));

        loanDetailsRepository.save(loanDetail);

        double principal = loanDetail.getLoanAmount();
        int tenureMonths = loanDetail.getTenure();
        double annualInterestRate = 0.12; // 12% annual interest
        double monthlyRate = annualInterestRate / 12;

        double emi = principal * monthlyRate / (1 - Math.pow(1 + monthlyRate, -tenureMonths));
        emi = BigDecimal.valueOf(emi).setScale(2, RoundingMode.HALF_UP).doubleValue();

        List<LoanConfirmResponse.EmiScheduleResponse> emiScheduleResponses = new ArrayList<>();
        List<EmiSchedule> emiEntities = new ArrayList<>();
        LocalDate startDate = LocalDate.parse(request.getPaymentDate());

        for (int i = 1; i <= tenureMonths; i++) {
            LocalDate paymentDate = startDate.plusMonths(i - 1);

            EmiSchedule emiEntity = new EmiSchedule();
            emiEntity.setLoanNumber(loanNumber);
            emiEntity.setEmiMonth(i);
            emiEntity.setEmiAmount(emi);
            emiEntity.setPaymentDate(java.sql.Date.valueOf(paymentDate));
            emiEntities.add(emiEntity);
            LoanConfirmResponse.EmiScheduleResponse emiResp = new LoanConfirmResponse.EmiScheduleResponse();
            emiResp.setEmiMonth(i);
            emiResp.setEmiAmount(emi);
            emiResp.setPaymentDate(paymentDate.toString());
            emiScheduleResponses.add(emiResp);
        }
        emiScheduleRepository.saveAll(emiEntities);
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setLoanNumber(loanDetail.getLoanNumber());
        transactionRequest.setTransactionId(loanDetail.getTransaction_token());
        TransactionDetailRequest transaction = new TransactionDetailRequest(
                loanDetail.getAccountNumber(),
                "Credit"
                ,
                loanDetail.getLoanAmount(),
                "Loan disbursement",
                LocalDate.now()
        );
        List<TransactionDetailRequest> transactions = new ArrayList<>();
        transactions.add(transaction);
        transactionRequest.setTransactions(transactions);

        isoClient.processMultiTransaction(transactionRequest);

        loanBookResponse.setStatus("Confirmed");
        redisTemplate.opsForValue().set(cacheKey, loanBookResponse, 10, TimeUnit.MINUTES);
        LoanConfirmResponse response = new LoanConfirmResponse();
        response.setLoanNumber(loanNumber);
        response.setStatus("Confirmed");
        response.setEmiSchedules(emiScheduleResponses);
        return ResponseUtility.getSuccessfulServerResponse(response, "Loan confirmed successfully");
    }

}
