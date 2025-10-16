package com.rashmita.isoservice.service;

import com.rashmita.commoncommon.model.TransactionRequest;

public interface BankIsoService {
   void processMultiTransaction(TransactionRequest request);
}
