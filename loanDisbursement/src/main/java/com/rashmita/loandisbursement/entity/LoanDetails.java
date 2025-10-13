package com.rashmita.loandisbursement.entity;

import com.rashmita.commoncommon.entity.AbstractEntity;
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

}
