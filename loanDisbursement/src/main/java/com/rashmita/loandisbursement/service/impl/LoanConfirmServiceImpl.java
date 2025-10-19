package com.rashmita.loandisbursement.service.impl;

import com.rashmita.commoncommon.entity.EmiSchedule;
import com.rashmita.commoncommon.entity.LoanDetails;
import com.rashmita.commoncommon.model.*;
import com.rashmita.commoncommon.repository.EmiScheduleRepository;
import com.rashmita.commoncommon.repository.LoanDetailsRepository;
import com.rashmita.loandisbursement.client.BankClient;
import com.rashmita.loandisbursement.client.IsoClient;
import com.rashmita.loandisbursement.model.LoanBookResponse;
import com.rashmita.loandisbursement.model.LoanConfirmRequest;
import com.rashmita.loandisbursement.model.LoanConfirmResponse;
import com.rashmita.loandisbursement.service.LoanConfirmService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class LoanConfirmServiceImpl implements LoanConfirmService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final LoanDetailsRepository loanDetailsRepository;
    private final EmiScheduleRepository emiScheduleRepository;
    private final IsoClient isoClient;
    private final BankClient bankClient;

    public LoanConfirmServiceImpl(RedisTemplate<String, Object> redisTemplate,
                                  LoanDetailsRepository loanDetailsRepository,
                                  EmiScheduleRepository emiScheduleRepository,
                                  IsoClient isoClient,
                                  BankClient bankClient) {
        this.redisTemplate = redisTemplate;
        this.loanDetailsRepository = loanDetailsRepository;
        this.emiScheduleRepository = emiScheduleRepository;
        this.isoClient = isoClient;
        this.bankClient = bankClient;
    }

    @Override
    public ServerResponse<?> confirmLoan(LoanConfirmRequest request) {

        // Retrieve loan booking info from Redis
        String cacheKey = "loanBook:" + request.getAccountNumber();
        LoanBookResponse loanBookResponse = (LoanBookResponse) redisTemplate.opsForValue().get(cacheKey);
        if (loanBookResponse == null) {
            return ResponseUtility.getFailedServerResponse("Loan booking not found or expired. Please complete loan booking first.");
        }
        if (!"Booked".equalsIgnoreCase(loanBookResponse.getStatus())) {
            return ResponseUtility.getFailedServerResponse("Loan cannot be confirmed. Current status: " + loanBookResponse.getStatus());
        }
        if (!loanBookResponse.getOtp().equals(request.getOtp())) {
            return ResponseUtility.getFailedServerResponse("Invalid OTP.");
        }

        // Generate unique 20-character loan number
        String loanNumber = "LN" + UUID.randomUUID().toString().replace("-", "").substring(0, 18).toUpperCase();

        // Fetch loan configuration
        LoanConfigurationResponse loanConfig = getLoanConfigurationByBankCode(request);

        // Save loan details
        LoanDetails loanDetail = new LoanDetails();
        loanDetail.setCode(UUID.randomUUID().toString());
        loanDetail.setBankCode(request.getBankCode());
        loanDetail.setLoanNumber(loanNumber);
        loanDetail.setTransaction_token(loanBookResponse.getTransactionToken());
        loanDetail.setCustomerNumber(request.getCustomerNumber());
        loanDetail.setAccountNumber(loanBookResponse.getAccountNumber());
        loanDetail.setLoanAmount(Double.parseDouble(request.getEmiAmount()));
        loanDetail.setTenure(Integer.parseInt(request.getEmiMonths()));
        loanDetail.setStatus("ACTIVE");
        loanDetail.setCreatedAt(new Date());
        loanDetail.setStartDate(java.sql.Date.valueOf(request.getPaymentDate()));
        loanDetail.setLoanAdministrationFeeAmount(loanConfig.getLoanAdministrationFeeAmount());
        loanDetail.setLoanAdministrationFeeRate(loanConfig.getLoanAdministrationFeeRate());
        loanDetail.setInterestRate(loanConfig.getInterestRate());
        loanDetail.setLateFeeCharge(loanConfig.getLateFeeCharge());
        loanDetail.setDefaultingPeriod(loanConfig.getDefaultingPeriod());
        loanDetail.setPenaltyInterest(loanConfig.getPenaltyInterest());
        loanDetail.setOverdueInterest(loanConfig.getOverdueInterest());
        loanDetailsRepository.save(loanDetail);

        // EMI Calculation
        double principal = loanDetail.getLoanAmount();
        int tenureMonths = loanDetail.getTenure();
        double annualInterestRate = loanConfig.getInterestRate();
        double emi = calculateEMI(principal, annualInterestRate, tenureMonths);
        emi = round(emi);

        List<EmiSchedule> emiEntities = new ArrayList<>();
        List<LoanConfirmResponse.EmiScheduleResponse> emiScheduleResponses = new ArrayList<>();

        LocalDate loanConfirmDate = LocalDate.now(); // Loan confirmation date
        LocalDate previousEmiDate = LocalDate.parse(request.getPaymentDate()); // 1st EMI payment date
        double remainingPrincipal = principal;
        double monthlyInterestRate = annualInterestRate / 1200;

        for (int i = 1; i <= tenureMonths; i++) {
            LocalDate emiStartDate = (i == 1) ? loanConfirmDate : previousEmiDate;
            LocalDate emiDate = (i == 1) ? previousEmiDate : previousEmiDate.plusMonths(1);

            double interestComponent = remainingPrincipal * monthlyInterestRate;
            double principalComponent = emi - interestComponent;
            remainingPrincipal -= principalComponent;

            // Adjust last installment
            if (i == tenureMonths && remainingPrincipal != 0) {
                principalComponent += remainingPrincipal;
                remainingPrincipal = 0;
            }

            // Save EMI schedule
            EmiSchedule emiEntity = new EmiSchedule();
            emiEntity.setLoanNumber(loanDetail.getLoanNumber());
            emiEntity.setLoanAmount(principal);
            emiEntity.setEmiMonth(i);
            emiEntity.setEmiAmount(round(emi));
            emiEntity.setPrincipalComponent(round(principalComponent));
            emiEntity.setInterestComponent(round(interestComponent));
            emiEntity.setRemainingAmount(round(remainingPrincipal));
            emiEntity.setEmiStartDate(emiStartDate);
            emiEntity.setEmiDate(emiDate);
            emiEntity.setStatus("PENDING");
            if (i == tenureMonths) emiEntity.setLastInstallment(true);
            emiEntities.add(emiEntity);

            // Response DTO
            LoanConfirmResponse.EmiScheduleResponse emiResp = new LoanConfirmResponse.EmiScheduleResponse();
            emiResp.setEmiMonth(i);
            emiResp.setEmiAmount(round(emi));
            emiResp.setStartDate(emiStartDate.toString());
            emiResp.setPaymentDate(emiDate.toString());
            emiResp.setPrincipalComponent(round(principalComponent));
            emiResp.setInterestComponent(round(interestComponent));
            emiResp.setRemainingAmount(round(remainingPrincipal));
            emiScheduleResponses.add(emiResp);

            previousEmiDate = emiDate; // update for next iteration
        }

        emiScheduleRepository.saveAll(emiEntities);

        // Trigger ISO transaction
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setLoanNumber(loanDetail.getLoanNumber());
        transactionRequest.setTransactionId(loanDetail.getTransaction_token());
        TransactionDetailRequest transaction = new TransactionDetailRequest(
                loanDetail.getAccountNumber(),
                "Credit",
                loanDetail.getLoanAmount(),
                "Loan disbursement",
                LocalDate.now()
        );
        transactionRequest.setTransactions(Collections.singletonList(transaction));
        isoClient.processMultiTransaction(transactionRequest);

        // Update Redis cache
        loanBookResponse.setStatus("Confirmed");
        redisTemplate.opsForValue().set(cacheKey, loanBookResponse, 10, TimeUnit.MINUTES);

        // Build response
        LoanConfirmResponse response = new LoanConfirmResponse();
        response.setLoanNumber(loanNumber);
        response.setStatus("Confirmed");
        response.setEmiSchedules(emiScheduleResponses);

        return ResponseUtility.getSuccessfulServerResponse(response, "Loan confirmed successfully");
    }

    private double calculateEMI(double principal, double annualInterestRate, int totalPeriods) {
        if (annualInterestRate == 0) return principal / totalPeriods;
        double monthlyInterestRate = (annualInterestRate / 100.0) / 12.0;
        return principal * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, totalPeriods)) /
                (Math.pow(1 + monthlyInterestRate, totalPeriods) - 1);
    }

    private double round(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private LoanConfigurationResponse getLoanConfigurationByBankCode(LoanConfirmRequest request) {
        LoanConfigBankCodeRequest configRequest = new LoanConfigBankCodeRequest();
        configRequest.setBankCode(request.getBankCode());
        ServerResponse<LoanConfigurationResponse> serverResponse = bankClient.getLoanConfigurationByBankCode(configRequest);
        if (serverResponse == null || serverResponse.getData() == null) {
            throw new RuntimeException("Loan configuration not found for bankCode: " + request.getBankCode());
        }
        return serverResponse.getData();
    }
}
