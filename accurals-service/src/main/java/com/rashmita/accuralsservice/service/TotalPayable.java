package com.rashmita.accuralsservice.service;

import com.rashmita.commoncommon.model.CreateTotalAccrual;
import com.rashmita.commoncommon.model.LoanNumberModel;

public interface TotalPayable {
    double calculateTotalOverdue(String loanNumber);
    double calculateTotalPenalty(String loanNumber);
    double calculateTotalLateFee(String loanNumber);
    double calculateTotalInterest(String loanNumber);
    double calculateTotalPayable(String loanNumber);
    CreateTotalAccrual calculateAccrualsByLoanNumber(LoanNumberModel loanNumber);
}
