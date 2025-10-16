package com.rashmita.accuralsservice.client;

import com.rashmita.commoncommon.model.*;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bank-service", url = "http://localhost:9096")
public interface BankClient {

    @PostMapping("customer/get/by/code/customernumber")
    CustomerResponse getCustomerByBankCodeAndCustomerNumber(@Valid @RequestBody BankIdAndCustomerRequest bankIdAndCustomerRequest);

    @PostMapping("/loanconfiguration/get/by/code")
    ServerResponse<LoanConfigurationResponse> getLoanConfigurationByBankCode(
            @RequestBody LoanConfigBankCodeRequest request);

}