package com.rashmita.settlementservice.client;

import com.rashmita.commoncommon.model.SettlementRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "iso-service", url = "http://localhost:9098")
public interface IsoClient {
    @PostMapping("/bank-iso/settle")
    String isoSettlement(@RequestBody SettlementRequest request);
}