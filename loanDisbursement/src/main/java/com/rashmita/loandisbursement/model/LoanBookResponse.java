package com.rashmita.loandisbursement.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanBookResponse {
    private String accountNumber;
    private String bankCode;
    private String transactionToken;
    private String status;
    //    private List<EmiCalculation> emi = new ArrayList<>();
    private String otp;
    private int emiMonths;
    private Double emiAmount;
    private Date paymentDate;

//    @Getter
//    @Setter
//    @NoArgsConstructor
//    public static class EmiCalculation {
//        private int emiMonths;
//        private double emiAmount;
//        private Date paymentDate;
//
//    }
}