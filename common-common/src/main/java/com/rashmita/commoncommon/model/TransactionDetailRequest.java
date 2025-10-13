package com.rashmita.commoncommon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailRequest {
    private String accountNumber;
    private String type; // DEBIT or CREDIT
    private Double amount;
    private String particularRemarks;
    private LocalDate valueDate;
}
