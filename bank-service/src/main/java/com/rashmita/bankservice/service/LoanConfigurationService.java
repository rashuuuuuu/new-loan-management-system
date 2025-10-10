package com.rashmita.bankservice.service;

import com.rashmita.bankservice.model.LoanConfigIdRequest;
import com.rashmita.bankservice.model.LoanConfigurationRequest;
import com.rashmita.bankservice.model.LoanUpdateRequest;
import com.rashmita.common.exception.NotFoundException;
import com.rashmita.common.model.ServerResponse;

public interface LoanConfigurationService {
    ServerResponse createLoanConfig(LoanConfigurationRequest loanConfigurationRequest);
    ServerResponse updateLoanConfig(LoanUpdateRequest loanUpdateRequest);
    ServerResponse deleteLoanConfig(LoanConfigIdRequest loanIdRequest);
    ServerResponse getLoanConfigById(LoanConfigIdRequest loanIdRequest) throws NotFoundException;

}
