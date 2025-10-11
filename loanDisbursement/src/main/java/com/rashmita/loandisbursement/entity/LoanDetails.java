package com.rashmita.loandisbursement.entity;

import com.rashmita.commoncommon.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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

    @Column(name="loan_tenure")
    private int loanTenure;

    @Column(name="loan_number",nullable = false)
    private String loanNumber;

    @Column(name="transaction_id",nullable=false)
    private String transactionId;

    @Column(name="created_at",nullable = false)
    private Date createdAt;

}
