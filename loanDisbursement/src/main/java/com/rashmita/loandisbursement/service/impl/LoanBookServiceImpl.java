package com.rashmita.loandisbursement.service.impl;

import com.rashmita.commoncommon.model.ResponseUtility;
import com.rashmita.commoncommon.model.ServerResponse;
import com.rashmita.loandisbursement.model.LoanBookRequest;
import com.rashmita.loandisbursement.model.LoanBookResponse;
import com.rashmita.loandisbursement.model.LoanProcessResponse;
import com.rashmita.loandisbursement.service.LoanBookService;
import com.rashmita.loandisbursement.service.LoanProcessService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class LoanBookServiceImpl implements LoanBookService {

    private final LoanProcessService loanProcessService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final OtpService otpService;

    public LoanBookServiceImpl(LoanProcessService loanProcessService,
                               RedisTemplate<String, Object> redisTemplate,
                               OtpService otpService) {
        this.loanProcessService = loanProcessService;
        this.redisTemplate = redisTemplate;
        this.otpService = otpService;
    }

    @Override
    public ServerResponse<LoanBookResponse> loanBook(LoanBookRequest request) {
        // Redis key for loan process
        String loanProcessKey = "loanProcess:" + request.getCustomerNumber();

        // Fetch LoanProcessResponse from Redis
        LoanProcessResponse loanProcessResponse =
                (LoanProcessResponse) redisTemplate.opsForValue().get(loanProcessKey);

        if (loanProcessResponse == null) {
            return ResponseUtility.getFailedServerResponse(
                    "Transaction not found or expired. Please initiate loan process again."
            );
        }

        // Validate loan process status
        if (!"Processed".equalsIgnoreCase(loanProcessResponse.getStatus())) {
            return ResponseUtility.getFailedServerResponse(
                    "Loan cannot be booked. Current status: " + loanProcessResponse.getStatus()
            );
        }

        // Validate transaction token and OTP
        if (!loanProcessResponse.getTransactionToken().equals(request.getTransactionToken())) {
            return ResponseUtility.getFailedServerResponse("Invalid transaction token.");
        }
        if (!loanProcessResponse.getOtp().equals(request.getOtp())) {
            return ResponseUtility.getFailedServerResponse("Invalid OTP.");
        }

        // Only validate account number now
        if (!Objects.equals(
                loanProcessResponse.getCreditScoreResponse().getAccountNumber(),
                request.getAccountNumber())) {
            return ResponseUtility.getFailedServerResponse("Invalid account number.");
        }

        // Prepare LoanBookResponse
        LoanBookResponse loanBookResponse = new LoanBookResponse();
        loanBookResponse.setOtp(otpService.generateOtp());
        loanBookResponse.setAccountNumber(loanProcessResponse.getCreditScoreResponse().getAccountNumber());
        loanBookResponse.setBankCode(loanProcessResponse.getCreditScoreResponse().getBankCode());
        loanBookResponse.setTransactionToken(request.getTransactionToken());
        loanBookResponse.setStatus("Booked");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date paymentDate = sdf.parse(request.getPaymentDate());

            LoanBookResponse.EmiCalculation emiCalc = new LoanBookResponse.EmiCalculation();
            emiCalc.setPaymentDate(paymentDate);
            loanBookResponse.getEmi().add(emiCalc);
        } catch (ParseException e) {
            return ResponseUtility.getFailedServerResponse(
                    "Invalid payment date format. Expected yyyy-MM-dd"
            );
        }
        String loanBookKey = "loanBook:" + request.getAccountNumber();
        redisTemplate.opsForValue().set(loanBookKey, loanBookResponse, 10, TimeUnit.MINUTES);

        return ResponseUtility.getSuccessfulServerResponse(loanBookResponse, "Loan booked successfully");
    }
}
