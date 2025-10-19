package com.rashmita.commoncommon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanReportDto {
    private String loanNumber;
    private String customerNumber;
    private String bankCode;
    private double totalInterest;
    private double totalLateFee;
    private double totalPenalty;
    private double totalOverdue;
    private double totalAccruals;
    private int emiMonth;
    private String status;
    private String emiStartDate;
    private String emiEndDate;
    private Map<Integer,Map<String,Object>> emiSummary;
}
