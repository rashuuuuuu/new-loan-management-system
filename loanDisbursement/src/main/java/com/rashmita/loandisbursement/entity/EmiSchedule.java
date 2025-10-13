package com.rashmita.loandisbursement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "emi_schedule")
@Getter
@Setter
@NoArgsConstructor
public class EmiSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loan_number", nullable = false)
    private String loanNumber;

    @Column(name = "emi_month", nullable = false)
    private int emiMonth;

    @Column(name = "emi_amount", nullable = false)
    private double emiAmount;

    @Column(name = "payment_date", nullable = false)
    private Date paymentDate;
}