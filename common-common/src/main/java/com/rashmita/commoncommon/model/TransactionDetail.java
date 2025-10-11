package com.rashmita.commoncommon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetail {
    private String accountNumber;
    private String type;
    private Double amount;
    private String particularRemarks;
    private LocalDate valueDate;
}