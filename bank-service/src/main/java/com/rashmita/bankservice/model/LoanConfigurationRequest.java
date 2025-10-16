package com.rashmita.bankservice.model;
import com.rashmita.common.model.BankIdRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class LoanConfigurationRequest {
    private int minimumAmount;
    private int maximumAmount;
    private int interestRate;
    private Double lateFeeCharge;
    private Double loanAdministrationFeeRate;
    private Double loanAdministrationFeeAmount;
    private int defaultingPeriod;
    private int maximumLoanPeriod;
    private BankIdRequest bankIdRequest;
    private int overdueInterestRate;
    private int penaltyInterestRate;

}
