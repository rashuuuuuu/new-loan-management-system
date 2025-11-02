package com.rashmita.common.entity;
import com.rashmita.common.constants.StatusConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "loan_configuration")
public class LoanConfiguration extends AbstractEntity {
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

    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Column(name="overdue_interest")
    private int overdueInterest;

    @Column(name="penalty_interest")
    private int penaltyInterest;

    @Column(name = "customer_status")
    @Enumerated(EnumType.STRING)
    private StatusConstants status;

    @OneToOne
    @JoinColumn(name = "bank_id", referencedColumnName = "id", nullable = false)
    private Bank bank;

    @Column(name="prepayment_charge_percentage")
    private int prepaymentChargePercentage;

    @Column(name="prepayment_charge_flat")
    private int prepaymentChargeFlat;


}
