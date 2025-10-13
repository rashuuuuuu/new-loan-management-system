//package com.rashmita.loandisbursement.service.impl;
//
//import com.rashmita.commoncommon.model.*;
//import com.rashmita.loandisbursement.client.BankClient;
//import com.rashmita.loandisbursement.client.CreditScoreClient;
//import com.rashmita.loandisbursement.model.CustomerResponse;
//import com.rashmita.loandisbursement.model.LoanConfigurationResponse;
//import com.rashmita.loandisbursement.model.LoanProcessRequest;
//import com.rashmita.loandisbursement.model.LoanProcessResponse;
//import com.rashmita.loandisbursement.service.LoanProcessService;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class LoanProcessServiceImpl implements LoanProcessService {
//
//    private final BankClient bankClient;
//    private final CreditScoreClient creditScoreClient;
//
//    public LoanProcessServiceImpl(BankClient bankClient, CreditScoreClient creditScoreClient) {
//        this.bankClient = bankClient;
//        this.creditScoreClient = creditScoreClient;
//    }
//
//    @Override
//    public ServerResponse<LoanProcessResponse> loanProcess(LoanProcessRequest loanProcessRequest) {
//        if (!validateCustomerRegistration(loanProcessRequest)) {
//            return ResponseUtility.getFailedServerResponse("Customer not registered or invalid");
//        }
//
//        CreditScoreResponse creditScoreResponse = getCreditScoreByAccountNumber(loanProcessRequest);
//
//        LoanConfigurationResponse loanConfig = getLoanConfigurationByBankCode(loanProcessRequest);
//
//        List<CreditScoreResponse.Tenure> eligibleTenures = creditScoreResponse.getTenures().stream()
//                .filter(t -> t.getEmiAmount() >= loanConfig.getMinimumAmount()
//                        && t.getEmiAmount() <= loanConfig.getMaximumAmount())
//                .collect(Collectors.toList());
//        if (eligibleTenures.isEmpty()) {
//            return ResponseUtility.getFailedServerResponse("No eligible EMI options found for the given limits");
//        }
//        creditScoreResponse.setTenures(eligibleTenures);
//        LoanProcessResponse loanProcessResponse = new LoanProcessResponse();
//        loanProcessResponse.setTransactionToken(UUID.randomUUID().toString());
//        loanProcessResponse.setCreditScoreResponse(creditScoreResponse);
//
//        return ResponseUtility.getSuccessfulServerResponse(loanProcessResponse, "Loan process successful");
//    }
//
//    private CustomerResponse getCustomerDetails(String bankCode, String customerNumber) {
//        BankIdAndCustomerRequest request = new BankIdAndCustomerRequest();
//        request.setBankCode(bankCode);
//        request.setCustomerNumber(customerNumber);
//        return bankClient.getCustomerByBankCodeAndCustomerNumber(request);
//    }
//
//    private boolean validateCustomerRegistration(LoanProcessRequest loanProcessRequest) {
//        CustomerResponse customerResponse = getCustomerDetails(
//                loanProcessRequest.getBankCode(),
//                loanProcessRequest.getCustomerNumber()
//        );
//        return customerResponse != null &&
//                loanProcessRequest.getAccountNumber().equals(customerResponse.getAccountNumber());
//    }
//
//    private CreditScoreResponse getCreditScoreByAccountNumber(LoanProcessRequest loanProcessRequest) {
//        CreditScoreByAccountNumber creditScoreRequest = new CreditScoreByAccountNumber();
//        creditScoreRequest.setBankCode(loanProcessRequest.getBankCode());
//        creditScoreRequest.setAccountNumber(loanProcessRequest.getAccountNumber());
//
//        ServerResponse<CreditScoreResponse> serverResponse =
//                creditScoreClient.getCustomerByBankCodeAndAccountNumber(creditScoreRequest);
//
//        if (serverResponse == null || serverResponse.getData() == null) {
//            throw new RuntimeException("Credit score not found for customer: " + loanProcessRequest.getCustomerNumber());
//        }
//
//        return serverResponse.getData();
//    }
//
//    private LoanConfigurationResponse getLoanConfigurationByBankCode(LoanProcessRequest loanProcessRequest) {
//        LoanConfigBankCodeRequest request = new LoanConfigBankCodeRequest();
//        request.setBankCode(loanProcessRequest.getBankCode());
//
//        ServerResponse<LoanConfigurationResponse> serverResponse =
//                bankClient.getLoanConfigurationByBankCode(request);
//
//        if (serverResponse == null || serverResponse.getData() == null) {
//            throw new RuntimeException("Loan configuration not found for bankCode: " + loanProcessRequest.getBankCode());
//        }
//
//        return serverResponse.getData();
//    }
//}
package com.rashmita.loandisbursement.service.impl;

import com.rashmita.commoncommon.model.*;
import com.rashmita.loandisbursement.client.BankClient;
import com.rashmita.loandisbursement.client.CreditScoreClient;
import com.rashmita.loandisbursement.model.*;
import com.rashmita.loandisbursement.service.LoanProcessService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class LoanProcessServiceImpl implements LoanProcessService {

    private final BankClient bankClient;
    private final CreditScoreClient creditScoreClient;
    private final RedisTemplate<String, Object> redisTemplate;

    public LoanProcessServiceImpl(BankClient bankClient,
                                  CreditScoreClient creditScoreClient,
                                  RedisTemplate<String, Object> redisTemplate) {
        this.bankClient = bankClient;
        this.creditScoreClient = creditScoreClient;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ServerResponse<LoanProcessResponse> loanProcess(LoanProcessRequest loanProcessRequest) {
        String cacheKey = "loanProcess:" + loanProcessRequest.getCustomerNumber();

        LoanProcessResponse cachedResponse = (LoanProcessResponse) redisTemplate.opsForValue().get(cacheKey);
        if (cachedResponse != null) {
            return ResponseUtility.getSuccessfulServerResponse(cachedResponse, "Loan process fetched from cache");
        }

        if (!validateCustomerRegistration(loanProcessRequest)) {
            return ResponseUtility.getFailedServerResponse("Customer not registered or invalid");
        }

        CreditScoreResponse creditScoreResponse = getCreditScoreByAccountNumber(loanProcessRequest);
        LoanConfigurationResponse loanConfig = getLoanConfigurationByBankCode(loanProcessRequest);

        List<CreditScoreResponse.Tenure> eligibleTenures = creditScoreResponse.getTenures().stream()
                .filter(t -> t.getEmiAmount() >= loanConfig.getMinimumAmount()
                        && t.getEmiAmount() <= loanConfig.getMaximumAmount())
                .collect(Collectors.toList());

        if (eligibleTenures.isEmpty()) {
            return ResponseUtility.getFailedServerResponse("No eligible EMI options found for the given limits");
        }

        creditScoreResponse.setTenures(eligibleTenures);

        LoanProcessResponse loanProcessResponse = new LoanProcessResponse();
        loanProcessResponse.setTransactionToken(UUID.randomUUID().toString());
        loanProcessResponse.setCreditScoreResponse(creditScoreResponse);
        OtpService otpService = new OtpService();
        loanProcessResponse.setStatus("Processed");
        loanProcessResponse.setOtp(otpService.generateOtp());

        //  Store response in Redis cache (expires in 10 minutes)
        redisTemplate.opsForValue().set(cacheKey, loanProcessResponse, 10, TimeUnit.MINUTES);

        return ResponseUtility.getSuccessfulServerResponse(loanProcessResponse, "Loan process successful");
    }

    private CustomerResponse getCustomerDetails(String bankCode, String customerNumber) {
        BankIdAndCustomerRequest request = new BankIdAndCustomerRequest();
        request.setBankCode(bankCode);
        request.setCustomerNumber(customerNumber);
        return bankClient.getCustomerByBankCodeAndCustomerNumber(request);
    }

    private boolean validateCustomerRegistration(LoanProcessRequest loanProcessRequest) {
        CustomerResponse customerResponse = getCustomerDetails(
                loanProcessRequest.getBankCode(),
                loanProcessRequest.getCustomerNumber()
        );
        return customerResponse != null &&
                loanProcessRequest.getAccountNumber().equals(customerResponse.getAccountNumber());
    }

    private CreditScoreResponse getCreditScoreByAccountNumber(LoanProcessRequest loanProcessRequest) {
        CreditScoreByAccountNumber creditScoreRequest = new CreditScoreByAccountNumber();
        creditScoreRequest.setBankCode(loanProcessRequest.getBankCode());
        creditScoreRequest.setAccountNumber(loanProcessRequest.getAccountNumber());

        ServerResponse<CreditScoreResponse> serverResponse =
                creditScoreClient.getCustomerByBankCodeAndAccountNumber(creditScoreRequest);

        if (serverResponse == null || serverResponse.getData() == null) {
            throw new RuntimeException("Credit score not found for customer: " + loanProcessRequest.getCustomerNumber());
        }

        return serverResponse.getData();
    }

    private LoanConfigurationResponse getLoanConfigurationByBankCode(LoanProcessRequest loanProcessRequest) {
        LoanConfigBankCodeRequest request = new LoanConfigBankCodeRequest();
        request.setBankCode(loanProcessRequest.getBankCode());

        ServerResponse<LoanConfigurationResponse> serverResponse =
                bankClient.getLoanConfigurationByBankCode(request);

        if (serverResponse == null || serverResponse.getData() == null) {
            throw new RuntimeException("Loan configuration not found for bankCode: " + loanProcessRequest.getBankCode());
        }

        return serverResponse.getData();
    }

}
