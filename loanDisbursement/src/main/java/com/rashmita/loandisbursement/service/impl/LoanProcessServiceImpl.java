package com.rashmita.loandisbursement.service.impl;

import com.rashmita.commoncommon.model.CreditScoreResponse;
import com.rashmita.commoncommon.model.ResponseUtility;
import com.rashmita.commoncommon.model.ServerResponse;
import com.rashmita.loandisbursement.client.BankClient;
import com.rashmita.loandisbursement.client.CreditScoreClient;
import com.rashmita.loandisbursement.model.CustomerResponse;
import com.rashmita.loandisbursement.model.LoanConfigurationResponse;
import com.rashmita.loandisbursement.model.LoanProcessRequest;
import com.rashmita.loandisbursement.model.LoanProcessResponse;
import com.rashmita.loandisbursement.service.LoanProcessService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class LoanProcessServiceImpl implements LoanProcessService {
    private final BankClient bankClient;
    private final CreditScoreClient creditScoreClient;

    public LoanProcessServiceImpl(BankClient bankClient, CreditScoreClient creditScoreClient) {
        this.bankClient = bankClient;
        this.creditScoreClient = creditScoreClient;
    }
    @Override
    public ServerResponse<LoanProcessResponse> loanProcess(LoanProcessRequest loanProcessRequest) {
        if (!validateCustomerRegistration(loanProcessRequest)) {
            return ResponseUtility.getFailedServerResponse("Customer not registered or invalid");
        }
        CreditScoreResponse creditScoreResponse = getCreditScoreByAccountNumber(loanProcessRequest);
        LoanConfigurationResponse loanConfigurationResponse = getLoanConfigurationByBankCode(loanProcessRequest);
        LoanProcessResponse loanProcessResponse = new LoanProcessResponse();
        loanProcessResponse.setBankCode(loanProcessRequest.getBankCode());
        loanProcessResponse.setOneMonthLimit(creditScoreResponse.getOneMonthLimit());

        List<Map<String, Object>> eligibleTenures = creditScoreResponse.getTenures().stream()
                .filter(t -> t.getEmiAmount() >= loanConfigurationResponse.getMinimumAmount()
                        && t.getEmiAmount() <= loanConfigurationResponse.getMaximumAmount())
                .map(t -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("tenure", t.getEmiMonths());
                    map.put("amount", t.getEmiAmount());
                    return map;
                })
                .collect(Collectors.toList());
        loanProcessResponse.setTenures(eligibleTenures);
        LoanProcessResponse.Data data = new LoanProcessResponse.Data();
        data.setTransactionToken(UUID.randomUUID().toString());
        data.setMinAmount(loanConfigurationResponse.getMinimumAmount());
        data.setMaxAmount(loanConfigurationResponse.getMaximumAmount());
        loanProcessResponse.setData(data);
        return ResponseUtility.getSuccessfulServerResponse(loanProcessResponse, "Loan process successful");
    }

    public CustomerResponse getCustomerDetails(String bankCode, String customerNumber) {
        return bankClient.getCustomerByBankCodeAndCustomerNumber(bankCode, customerNumber);
    }


    private Boolean validateCustomerRegistration(LoanProcessRequest loanProcessRequest) {
        String accountNumber = loanProcessRequest.getAccountNumber();
        if (accountNumber.equals(getCustomerDetails(loanProcessRequest.getBankCode(), loanProcessRequest.getCustomerNumber()).getAccountNumber())) {
            return true;
        } else {
            return false;
        }
    }

    private CreditScoreResponse getCreditScoreByAccountNumber(LoanProcessRequest loanProcessRequest) {
        // Call the Feign client
        ServerResponse<CreditScoreResponse> serverResponse =
                creditScoreClient.getCustomerByBankCodeAndAccountNumber(
                        loanProcessRequest.getBankCode(),
                        loanProcessRequest.getCustomerNumber()
                );

        if (serverResponse != null && serverResponse.getData() != null) {
            return serverResponse.getData();
        } else {
            throw new RuntimeException("Credit score not found for customer: " + loanProcessRequest.getCustomerNumber());
        }
    }

    private LoanConfigurationResponse getLoanConfigurationByBankCode(LoanProcessRequest loanProcessRequest) {
        ServerResponse<LoanConfigurationResponse> serverResponse =
                bankClient.getLoanConfigurationByBankCode(loanProcessRequest.getBankCode());
        if (serverResponse != null && serverResponse.getData() != null) {
            return serverResponse.getData();
        } else {
            throw new RuntimeException("Loan configuration not found for bankCode: " + loanProcessRequest.getBankCode());
        }
    }


}
