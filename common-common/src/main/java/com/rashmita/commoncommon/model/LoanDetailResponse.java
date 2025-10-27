package com.rashmita.commoncommon.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class LoanDetailResponse {
    private String accountNumber;
    private String bankCode;
    private String code;
    private String customerNumber;
    private Integer defaultingPeriod;
    private Integer interestRate;
    private Double lateFeeCharge;
    private Double loanAdministrationFeeAmount;
    private Double loanAdministrationFeeRate;
    private Double loanAmount;
    private String loanNumber;
    private Integer maximumAmount;
    private Integer maximumLoanPeriod;
    private Integer minimumAmount;
    private Integer overdueInterest;
    private Integer penaltyInterest;
    private String status;
    private Integer tenure;
}
