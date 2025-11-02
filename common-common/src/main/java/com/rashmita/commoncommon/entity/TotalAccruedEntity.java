package com.rashmita.commoncommon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "total_accrued")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalAccruedEntity extends AbstractEntity{
    private String loanNumber;
    private int tenure;
    private Double emiAmount;
    private Double payableInterest;
    private Double payablePenalty;
    private Double payableOverdue;
    private Double payableLateFee;
    private Double totalPayable;
}
