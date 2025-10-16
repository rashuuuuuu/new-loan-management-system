package com.rashmita.loandisbursement.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanBookRequest {
    private String accountNumber;
    private String bankCode;
    private String customerNumber;
    private String transactionToken;
    private String oneMonthEmi;
    private int emiMonths;
    private Double emiAmount;
    private String paymentDate;
    private String otp;
}
