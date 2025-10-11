package com.rashmita.loandisbursement.client;

import com.rashmita.commoncommon.model.ServerResponse;
import com.rashmita.loandisbursement.model.CustomerResponse;
import com.rashmita.loandisbursement.model.LoanConfigurationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "bank-service", url = "http://localhost:9096")
public interface BankClient {

    // Correct path variable annotations
    @GetMapping("/customer/get/by/code/{bankCode}/{customerNumber}")
    CustomerResponse getCustomerByBankCodeAndCustomerNumber(
            @PathVariable("bankCode") String bankCode,
            @PathVariable("customerNumber") String customerNumber);

    @GetMapping("/loanconfiguration/get/by/{bankCode}") // add leading slash
    ServerResponse<LoanConfigurationResponse> getLoanConfigurationByBankCode(
            @PathVariable("bankCode") String bankCode);
}