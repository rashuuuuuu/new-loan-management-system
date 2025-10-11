package com.rashmita.bankservice.service;

import com.rashmita.bankservice.model.LoanConfigBankCodeRequest;
import com.rashmita.bankservice.model.LoanConfigurationRequest;
import com.rashmita.bankservice.model.LoanUpdateRequest;
import com.rashmita.common.exception.NotFoundException;
import com.rashmita.common.model.ServerResponse;

public interface LoanConfigurationService {
    ServerResponse createLoanConfig(LoanConfigurationRequest loanConfigurationRequest);
    ServerResponse updateLoanConfig(LoanUpdateRequest loanUpdateRequest);
    ServerResponse deleteLoanConfig(LoanConfigBankCodeRequest loanIdRequest);
    ServerResponse getLoanConfigByBankCode(LoanConfigBankCodeRequest loanIdRequest) throws NotFoundException;

}
