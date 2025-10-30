package com.rashmita.commoncommon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "emi_penalty",
        uniqueConstraints = @UniqueConstraint(columnNames = {"loan_number", "accrual_date"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmiPenalty extends AbstractEntity{
    private String loanNumber;
    private Long emiId;
    @Column(name = "accrual_date",unique = true)
    private LocalDate accrualDate;
    private Double penaltyAmount;
    private int emiMonth;
}
