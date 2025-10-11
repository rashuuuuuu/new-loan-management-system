package com.rashmita.isoservice.service;

import com.rashmita.commoncommon.model.FundTransferRequest;
import com.rashmita.commoncommon.model.FundTransferResponse;

public interface BankIsoService {
   public  FundTransferResponse transferFunds(FundTransferRequest request);
}
