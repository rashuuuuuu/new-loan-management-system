package com.rashmita.accuralsservice.service;

import com.rashmita.commoncommon.model.CreateTotalAccrual;
import com.rashmita.commoncommon.model.LoanNumberModel;
import com.rashmita.commoncommon.model.LoanReportDto;
import java.util.List;
public interface TotalPayable {
    double calculateTotalOverdue(String loanNumber);
    double calculateTotalPenalty(String loanNumber);
    double calculateTotalLateFee(String loanNumber);
    double calculateTotalInterest(String loanNumber);
//    double calculateTotalPayable(String loanNumber);
    List<LoanReportDto> getAllReport();
    CreateTotalAccrual calculateAccrualsByLoanNumber(LoanNumberModel loanNumber);
    String totalPayablePerMonth(LoanNumberModel loanNumber);

}
