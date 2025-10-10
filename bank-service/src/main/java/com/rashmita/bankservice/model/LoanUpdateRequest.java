package com.rashmita.bankservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class LoanUpdateRequest {
    private Long id;
    private int minimumAmount;
    private int maximumAmount;
    private Double interestRate;
    private Double lateFeeCharge;
    private Double loanAdministrationFeeRate;
    private Double loanAdministrationFeeAmount;
    private int defaultingPeriod;
    private int maximumLoanPeriod;
    private Date modifiedDate;
}
