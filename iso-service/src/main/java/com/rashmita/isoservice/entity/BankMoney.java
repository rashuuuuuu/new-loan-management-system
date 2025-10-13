package com.rashmita.isoservice.entity;

import com.rashmita.commoncommon.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bank_money")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankMoney extends AbstractEntity {

    @Column(name = "account_number", nullable = false, length = 50)
    private String accountNumber;

    @Column(nullable = false, length = 10)
    private String type; // DEBIT or CREDIT

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "transfer_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal transferAmount;

    @Column(name = "particular_remarks", length = 255)
    private String particularRemarks;

    @Column(name = "value_date", nullable = false)
    private LocalDate valueDate;

    @Column(name = "total_balance", nullable = false, precision = 20, scale = 2)
    private BigDecimal totalBalance;
}
