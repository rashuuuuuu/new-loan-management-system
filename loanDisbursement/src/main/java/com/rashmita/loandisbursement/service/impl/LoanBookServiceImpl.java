package com.rashmita.loandisbursement.service.impl;
import com.rashmita.commoncommon.model.CreditScoreByAccountNumber;
import com.rashmita.commoncommon.model.CreditScoreResponse;
import com.rashmita.commoncommon.model.ResponseUtility;
import com.rashmita.commoncommon.model.ServerResponse;
import com.rashmita.loandisbursement.client.CreditScoreClient;
import com.rashmita.loandisbursement.model.LoanBookRequest;
import com.rashmita.loandisbursement.model.LoanBookResponse;
import com.rashmita.loandisbursement.model.LoanProcessResponse;
import com.rashmita.loandisbursement.service.LoanBookService;
import com.rashmita.loandisbursement.service.LoanProcessService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class LoanBookServiceImpl implements LoanBookService {
    private final LoanProcessService loanProcessService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final OtpService otpService;
    private final CreditScoreClient creditScoreClient;

    public LoanBookServiceImpl(LoanProcessService loanProcessService,
                               RedisTemplate<String, Object> redisTemplate,
                               OtpService otpService, CreditScoreClient creditScoreClient) {
        this.loanProcessService = loanProcessService;
        this.redisTemplate = redisTemplate;
        this.otpService = otpService;
        this.creditScoreClient = creditScoreClient;
    }

    @Override
    public ServerResponse<LoanBookResponse> loanBook(LoanBookRequest request) {
        String loanProcessKey = "loanProcess:" + request.getCustomerNumber();
        CreditScoreByAccountNumber creditScoreByAccountNumber = new CreditScoreByAccountNumber();
        creditScoreByAccountNumber.setAccountNumber(request.getAccountNumber());
        creditScoreByAccountNumber.setBankCode(request.getBankCode());
        ServerResponse<CreditScoreResponse> creditScoreResponse = creditScoreClient.getCustomerByBankCodeAndAccountNumber(creditScoreByAccountNumber);
        if (!creditScoreResponse.getData().getEmiMaxAmount().equals((request.getEmiAmount()))) {
            return ResponseUtility.getSuccessfulServerResponse("customer is not eligible for this tenure");
        }
        LoanProcessResponse loanProcessResponse =
                (LoanProcessResponse) redisTemplate.opsForValue().get(loanProcessKey);
        if (loanProcessResponse == null) {
            return ResponseUtility.getFailedServerResponse(
                    "Transaction not found or expired. Please initiate loan process again."
            );
        }
        if (!"Processed".equalsIgnoreCase(loanProcessResponse.getStatus())) {
            return ResponseUtility.getFailedServerResponse(
                    "Loan cannot be booked. Current status: " + loanProcessResponse.getStatus()
            );
        }
        if (!loanProcessResponse.getTransactionToken().equals(request.getTransactionToken())) {
            return ResponseUtility.getFailedServerResponse("Invalid transaction token.");
        }
        if (!loanProcessResponse.getOtp().equals(request.getOtp())) {
            return ResponseUtility.getFailedServerResponse("Invalid OTP.");
        }
        if (!Objects.equals(
                loanProcessResponse.getCreditScoreResponse().getAccountNumber(),
                request.getAccountNumber())) {
            return ResponseUtility.getFailedServerResponse("Invalid account number.");
        }
        LoanBookResponse loanBookResponse = new LoanBookResponse();
        loanBookResponse.setOtp(otpService.generateOtp());
        loanBookResponse.setAccountNumber(loanProcessResponse.getCreditScoreResponse().getAccountNumber());
        loanBookResponse.setBankCode(loanProcessResponse.getCreditScoreResponse().getBankCode());
        loanBookResponse.setTransactionToken(request.getTransactionToken());
        loanBookResponse.setEmiMonths(request.getEmiMonths());
        loanBookResponse.setEmiAmount(request.getEmiAmount());
        loanBookResponse.setStatus("Booked");
        String loanBookKey = "loanBook:" + request.getAccountNumber();
        redisTemplate.opsForValue().set(loanBookKey, loanBookResponse, 10, TimeUnit.MINUTES);
        return ResponseUtility.getSuccessfulServerResponse(loanBookResponse, "Loan booked successfully");
    }
}
