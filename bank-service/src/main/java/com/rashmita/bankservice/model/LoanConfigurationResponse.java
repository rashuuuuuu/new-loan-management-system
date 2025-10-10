package com.rashmita.bankservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class LoanConfigurationResponse {
    private String minimumAmount;
    private String maximumAmount;
    private Double interestRate;
    private Double lateFeeCharge;
    private Double loanAdministrationFeeRate;
    private Double loanAdministrationFeeAmount;
    private int defaultingPeriod;
    private int maximumLoanPeriod;
    private Date modifiedDate;
}
