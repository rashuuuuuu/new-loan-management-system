package com.rashmita.isoservice.entity;

import com.rashmita.commoncommon.entity.AbstractEntity;
import com.rashmita.isoservice.Aspect.UniqueTransactionId;
import com.rashmita.isoservice.constants.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetail extends AbstractEntity {
    private String loanNumber;
    private String transactionId;
    private String accountNumber;
    private String fromAccount;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private BigDecimal totalAmount;
    private BigDecimal transferAmount;
    private String particularRemarks;
    private LocalDate valueDate;
    private LocalDateTime createdAt = LocalDateTime.now();
}
