package com.rashmita.settlementservice.service.Impl;

import com.rashmita.commoncommon.model.SettlementRequest;
import com.rashmita.settlementservice.client.IsoClient;
import com.rashmita.settlementservice.service.SettlementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Service
@Slf4j
public class SettlementServiceImpl implements SettlementService {
    private final IsoClient isoClient;

    public SettlementServiceImpl(IsoClient isoClient) {
        this.isoClient = isoClient;
    }

    @Override
    public void createSettlement(SettlementRequest request) {
        SettlementRequest settlementRequest = new SettlementRequest();
        settlementRequest.setLoanNumber(request.getLoanNumber());
        settlementRequest.setTransactionId(request.getTransactionId());
        settlementRequest.setTransactionId(request.getTransactionId());
        settlementRequest.setAccountNumber(request.getAccountNumber());
        settlementRequest.setAmount(request.getAmount());
        settlementRequest.setEmiMonth(request.getEmiMonth());
        settlementRequest.setTransactions(request.getTransactions());
        isoClient.isoSettlement(settlementRequest);
    }
}
