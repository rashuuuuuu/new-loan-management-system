package com.rashmita.commoncommon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmiPenalty extends AbstractEntity{
    @Column(name="loan_number")
    private String loanNumber;
    private Long emiId;
    @Column(name="accrual_date")
    private LocalDate accrualDate;
    private Double penaltyAmount;
    @Column(name="emi_month")
    private int emiMonth;
}
