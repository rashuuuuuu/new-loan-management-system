package com.rashmita.bankservice.service.serviceImpl;

import com.rashmita.bankservice.mapper.LoanConfigurationMapper;
import com.rashmita.bankservice.model.LoanConfigBankCodeRequest;
import com.rashmita.bankservice.model.LoanConfigurationRequest;
import com.rashmita.bankservice.model.LoanUpdateRequest;
import com.rashmita.bankservice.service.LoanConfigurationService;
import com.rashmita.common.exception.NotFoundException;
import com.rashmita.common.model.ResponseUtility;
import com.rashmita.common.model.ServerResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanConfigurationServiceImpl implements LoanConfigurationService {
    @Autowired
    private LoanConfigurationMapper loanConfigurationMapper;

    @Override
    public ServerResponse<?> createLoanConfig(LoanConfigurationRequest loanConfigurationRequest) {
        loanConfigurationMapper.saveLoanConfiguration(loanConfigurationRequest);
        return ResponseUtility.getSuccessfulServerResponse("loan configuration Created Successfully");
    }

    @Override
    public ServerResponse<?> updateLoanConfig(LoanUpdateRequest loanUpdateRequest) {
        loanConfigurationMapper.updateLoanConfiguration(loanUpdateRequest);
        return ResponseUtility.getSuccessfulServerResponse("loan configuration Updated Successfully");
    }

    @Override
    public ServerResponse<?> deleteLoanConfig(LoanConfigBankCodeRequest loanConfigBankCodeRequest) {
        loanConfigurationMapper.deleteLoanConfiguration(loanConfigBankCodeRequest);
        return ResponseUtility.getSuccessfulServerResponse("loan Configuration  Deleted Successfully");
    }

    @Override
    public ServerResponse<?> getLoanConfigByBankCode(LoanConfigBankCodeRequest loanConfigBankCodeRequest) throws NotFoundException {
        loanConfigurationMapper.getDetailsByBankCode(loanConfigBankCodeRequest);
        return ResponseUtility.getSuccessfulServerResponse("loan Configuration details  fetched Successfully");
    }
}
