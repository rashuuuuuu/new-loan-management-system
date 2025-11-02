package com.rashmita.prepaymentservice.client;

import com.rashmita.commoncommon.model.TransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "iso-service", url = "http://localhost:9098")
public interface IsoClient {
    @PostMapping("/bank-iso/transfer")
    String processMultiTransaction(@RequestBody TransactionRequest request);
    @PostMapping("/bank-iso/prepayment")
    String isoPrepayment(@RequestBody TransactionRequest request);


}