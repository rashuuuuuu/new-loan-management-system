package com.rashmita.commoncommon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "prepayment_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrepaymentDetails extends AbstractEntity{
    private String loanNumber;
    private Double prePaymentCharge;
    private Double paidAmount;
    private LocalDate prepaymentDate;
}
