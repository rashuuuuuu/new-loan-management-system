package com.rashmita.loandisbursement.client;

import com.rashmita.commoncommon.model.CreditScoreResponse;
import com.rashmita.commoncommon.model.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "creditscore-service", url = "http://localhost:9099") // replace port with your service port
public interface CreditScoreClient {
    @GetMapping("/credit-score/get/by/{bankCode}/{customerNumber}")
    ServerResponse<CreditScoreResponse> getCustomerByBankCodeAndAccountNumber(
            @PathVariable("bankCode") String bankCode,
            @PathVariable("customerNumber") String customerNumber
    );
}