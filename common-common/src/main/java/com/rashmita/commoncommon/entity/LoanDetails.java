package com.rashmita.commoncommon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "loan_detail")
public class LoanDetails extends AbstractEntity {
    @Column(name="code",nullable = false)
    private String code;

    @Column(name="customer_number",nullable = false)
    private String customerNumber;

    @Column(name="account_number",nullable = false)
    private String accountNumber;

    @Column(name="bank_code",nullable=false)
    private String bankCode;

    @Column(name="loan_amount",nullable=false)
    private Double loanAmount;

    @Column(name = "tenure", nullable = false)
    private int tenure;

    @Column(name="loan_number",nullable = false)
    private String loanNumber;

    @Column(name="transaction_token",nullable=false)
    private String transaction_token;

    @Column(name="created_at",nullable = false)
    private Date createdAt;

    @Column(name="status",nullable = false)
    private String status;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name="minimum_amount",nullable = false)
    private int minimumAmount;

    @Column(name="maximum_amount",nullable = false)
    private int maximumAmount;

    @Column(name="interest_rate",nullable = false)
    private int interestRate;

    @Column(name="late_fee_charge",nullable = false)
    private Double lateFeeCharge;

    @Column(name = "loan_administration_fee_rate")
    private Double loanAdministrationFeeRate;

    @Column(name = "loan_administration_fee_amount")
    private Double loanAdministrationFeeAmount;

    @Column(name = "defaulting_period", nullable = false)
    private int defaultingPeriod;

    @Column(name = "maximum_loan_period", nullable = false)
    private int maximumLoanPeriod;

    @Column(name="penalty_interest")
    private int penaltyInterest;

    @Column(name="overdue_interest")
    private int overdueInterest;


}
