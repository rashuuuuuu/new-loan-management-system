package com.rashmita.loandisbursement.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanBookResponse {
    private String accountNumber;
    private String bankCode;
    private String transactionToken;
    private String status;
    private List<EmiCalculation> emi = new ArrayList<>();
    private String otp;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class EmiCalculation {
        private int emiMonths;
        private double emiAmount;
        private Date paymentDate;

    }
}