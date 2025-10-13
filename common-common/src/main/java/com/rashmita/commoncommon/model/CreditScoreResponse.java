package com.rashmita.commoncommon.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreditScoreResponse {
    private String bankCode;
    private String accountNumber;
    private String mobileNumber;
    private Double oneMonthLimit;
    private Double emiMaxAmount;
    private List<Tenure> tenures = new ArrayList<>();

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
