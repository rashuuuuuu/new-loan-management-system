package com.rashmita.commoncommon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "emi_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmiSchedule extends AbstractEntity {
    private String loanNumber;
    private LocalDate emiDate;
    private LocalDate emiStartDate;
    private Double emiAmount;
    private int emiMonth;
    private Double principalComponent;
    private Double interestComponent;
    private Double paidAmount;
    private LocalDateTime paidDate;
    private String status;
    private Boolean lastInstallment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double remainingAmount;
    private Double loanAmount;

}