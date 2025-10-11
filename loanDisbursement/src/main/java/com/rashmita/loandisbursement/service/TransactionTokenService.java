package com.rashmita.loandisbursement.service;

import com.rashmita.commoncommon.model.GetClaims;
import com.rashmita.commoncommon.model.TokenPayload;

public interface TransactionTokenService {
    String generateToken(TokenPayload tokenPayload);
    String updateToken(TransactionTokenService transactionTokenService, TokenPayload tokenPayload);
    TransactionTokenService getTransactionToken(GetClaims getClaims);

}
