package com.rashmita.loandisbursement.client;

import com.rashmita.commoncommon.model.CreditScoreByAccountNumber;
import com.rashmita.commoncommon.model.CreditScoreResponse;
import com.rashmita.commoncommon.model.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "creditscore-service", url = "http://localhost:9099") // replace port with your service port
public interface CreditScoreClient {
    @PostMapping("/creditscore/get/by/accountnumber") // matches controller
    ServerResponse<CreditScoreResponse> getCustomerByBankCodeAndAccountNumber(
            @RequestBody CreditScoreByAccountNumber request
    );
}