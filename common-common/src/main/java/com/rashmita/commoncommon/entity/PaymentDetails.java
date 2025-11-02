package com.rashmita.commoncommon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "payment_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails extends AbstractEntity {
    private int installmentNumber;
    private LocalDate demandDate;
    private String status;
    private Double emiAmount;
    private Double paidPrincipal;
    private Double PayablePrincipal;
    private Double paidInterest;
    private Double payableInterest;
    private Double paidOverdue;
    private Double payableOverdue;
    private Double paidPenalty;
    private Double payablePenalty;
    private Double paidLateFee;
    private Double payableLateFee;
}
