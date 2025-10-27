package com.rashmita.loandisbursement.service.impl;

import com.rashmita.commoncommon.model.LoanDetailResponse;
import com.rashmita.commoncommon.model.ResponseUtility;
import com.rashmita.commoncommon.model.ServerResponse;
import com.rashmita.loandisbursement.mapper.LoanDetailMapper;
import com.rashmita.loandisbursement.service.LoanDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanDetailServiceImpl implements LoanDetailService {
    private final LoanDetailMapper loanDetailMapper;
    @Override
    public ServerResponse<?> getAllLoanDetails() {
       return ResponseUtility.getSuccessfulServerResponse(loanDetailMapper.getAllLoanDetails(),"Loan Details fetched successfully");
    }
}
