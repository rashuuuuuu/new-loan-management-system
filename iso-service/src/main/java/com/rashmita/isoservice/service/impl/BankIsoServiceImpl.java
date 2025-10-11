package com.rashmita.isoservice.service.impl;

import com.rashmita.commoncommon.model.FundTransferRequest;
import com.rashmita.commoncommon.model.FundTransferResponse;
import com.rashmita.isoservice.service.BankIsoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankIsoServiceImpl implements BankIsoService {

    public FundTransferResponse transferFunds(FundTransferRequest request) {
        Double debitAccount;
        Double creditAccount;


}
