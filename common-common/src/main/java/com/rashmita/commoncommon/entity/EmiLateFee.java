package com.rashmita.commoncommon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "emi_late_fee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmiLateFee extends AbstractEntity {
    private String loanNumber;
    private Long emiId;
    private Double lateFee;
    private LocalDate accrualDate;
    private int emiMonth;
}
