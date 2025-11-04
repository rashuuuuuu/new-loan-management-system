package com.rashmita.isoservice.client;

import com.rashmita.commoncommon.entity.AmountUpdateRequest;
import com.rashmita.commoncommon.model.CustomerResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bank-service", url = "http://localhost:9096")
public interface BankClient {
    @PostMapping("customer/update/by/customernumber")
    CustomerResponse updateAmountByCustomerNumber(@Valid @RequestBody AmountUpdateRequest amountUpdateRequest );
}
