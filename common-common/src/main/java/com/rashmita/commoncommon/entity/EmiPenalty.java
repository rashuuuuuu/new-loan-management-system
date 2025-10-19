package com.rashmita.commoncommon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "emi_penalty")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmiPenalty extends AbstractEntity{
    private String loanNumber;
    private Long emiId;
    private LocalDate accrualDate;
    private Double penaltyAmount;
    private int emiMonth;
}
