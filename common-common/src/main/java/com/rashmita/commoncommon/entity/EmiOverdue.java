package com.rashmita.commoncommon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "emi_overdue")
@Entity
public class EmiOverdue extends AbstractEntity {
    private Long emiId;
    private String loanNumber;
    private LocalDate accrualDate;
    private Double overdueAmount;
    private int emiMonth;
}
