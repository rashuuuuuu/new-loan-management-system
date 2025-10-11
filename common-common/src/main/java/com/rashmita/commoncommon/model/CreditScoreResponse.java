package com.rashmita.commoncommon.model;

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

    // List of tenure objects
    private List<Tenure> tenures;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Tenure {
        private int emiMonths;
        private double emiAmount;

        public Tenure(int emiMonths, double emiAmount) {
            this.emiMonths = emiMonths;
            this.emiAmount = emiAmount;
        }
    }
}
