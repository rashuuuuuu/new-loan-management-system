package com.rashmita.settlementservice.service;

import com.rashmita.commoncommon.model.SettlementRequest;

public interface SettlementService {
    void createSettlement(SettlementRequest settlementRequest);
}
