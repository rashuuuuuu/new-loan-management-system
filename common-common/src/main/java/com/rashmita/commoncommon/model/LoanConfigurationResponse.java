package com.rashmita.commoncommon.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class LoanConfigurationResponse {
    private Double minimumAmount;
    private Double maximumAmount;
    private int interestRate;
    private Double lateFeeCharge;
    private Double loanAdministrationFeeRate;
    private Double loanAdministrationFeeAmount;
    private int defaultingPeriod;
    private int maximumLoanPeriod;
    private Date modifiedDate;
    private int penaltyInterest;
    private int overdueInterest;
}
