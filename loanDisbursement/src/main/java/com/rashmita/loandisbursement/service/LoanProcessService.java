package com.rashmita.loandisbursement.service;

import com.rashmita.commoncommon.model.ServerResponse;
import com.rashmita.loandisbursement.model.LoanProcessRequest;

public interface LoanProcessService {
public ServerResponse<?> loanProcess(LoanProcessRequest loanProcessRequest);
}
