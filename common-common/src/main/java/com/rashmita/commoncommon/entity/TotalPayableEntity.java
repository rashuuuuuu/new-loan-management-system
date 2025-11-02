package com.rashmita.commoncommon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "total_payable")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalPayableEntity extends AbstractEntity{
    private int tenure;
    private String status;
    private LocalDate emiDate;
    private String loanNumber;
    private Double emiAmount;
    private Double payablePrincipal;
    private Double paidPrincipal;
    private Double payableInterest;
    private Double paidInterest;
    private Double payablePenalty;
    private Double paidPenalty;
    private Double payableOverdue;
    private Double paidOverdue;
    private Double payableLateFee;
    private Double paidLateFee;
    private Double totalPayable;
}
