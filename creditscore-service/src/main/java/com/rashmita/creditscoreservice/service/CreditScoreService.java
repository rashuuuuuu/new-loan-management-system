package com.rashmita.creditscoreservice.service;

import com.rashmita.commoncommon.exception.NotFoundException;
import com.rashmita.commoncommon.model.ServerResponse;
import com.rashmita.creditscoreservice.model.CreditScoreRequest;
public interface CreditScoreService {
    ServerResponse<?> getCreditScoreByAccountNumber(CreditScoreRequest creditScoreRequest) throws NotFoundException;
    ServerResponse<?> getCustomerByBankCodeAndAccountNumber(String bankCode, String customerNumber) throws NotFoundException;
}
