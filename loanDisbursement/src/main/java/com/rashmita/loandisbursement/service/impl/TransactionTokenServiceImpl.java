package com.rashmita.loandisbursement.service.impl;

import com.rashmita.commoncommon.model.GetClaims;
import com.rashmita.commoncommon.model.TokenPayload;
import com.rashmita.loandisbursement.service.TransactionTokenService;

public class TransactionTokenServiceImpl implements TransactionTokenService {

    @Override
    public String generateToken(TokenPayload tokenPayload) {
        return "";
    }

    @Override
    public String updateToken(TransactionTokenService transactionTokenService, TokenPayload tokenPayload) {
        return "";
    }

    @Override
    public TransactionTokenService getTransactionToken(GetClaims getClaims) {
        return null;
    }
}
