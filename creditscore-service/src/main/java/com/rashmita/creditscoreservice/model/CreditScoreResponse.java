package com.rashmita.creditscoreservice.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreditScoreResponse {
    private String accountNumber;
    private String customerNumber;
    private String mobileNumber;
    private Double oneMonthLimit;
    private Double emiMonth;
    private Double emiMaxAmount;

    // Add a list of tenures
    private List<Tenure> tenures;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Tenure {
        private int emiMonths;
        private double emiAmount;
    }
}