package com.rashmita.bankservice.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class LoanConfigurationRequest {
    private int minimumAmount;
    private int maximumAmount;
    private Double interestRate;
    private Double lateFeeCharge;
    private Double loanAdministrationFeeRate;
    private Double loanAdministrationFeeAmount;
    private int defaultingPeriod;
    private int maximumLoanPeriod;
}
