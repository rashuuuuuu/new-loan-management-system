package com.rashmita.commoncommon.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "emi_interest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmiInterest extends AbstractEntity {
        private String loanNumber;
        private Long emiId;
        private LocalDate accrualDate;
        private Double interestAmount;
    }

