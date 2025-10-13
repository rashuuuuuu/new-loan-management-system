package com.rashmita.loandisbursement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanConfirmRequest {
    private String customerNumber;
    private String accountNumber;
    private String bankCode;
    private String transactionToken;
    private String otp;
    private String emiMonths;
    private String emiAmount;
    private String paymentDate;
}
