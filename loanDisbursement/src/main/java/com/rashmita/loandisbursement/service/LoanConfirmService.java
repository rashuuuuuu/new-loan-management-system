package com.rashmita.loandisbursement.service;

import com.rashmita.commoncommon.model.ServerResponse;
import com.rashmita.loandisbursement.model.LoanConfirmRequest;

public interface LoanConfirmService {
    ServerResponse<?> confirmLoan(LoanConfirmRequest loanConfirmRequest);
}
