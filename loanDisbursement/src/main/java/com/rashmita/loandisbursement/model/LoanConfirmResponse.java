package com.rashmita.loandisbursement.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class LoanConfirmResponse {
    private String loanNumber;
    private String status;
    private List<EmiScheduleResponse> emiSchedules;
    @Getter
    @Setter
    public static class EmiScheduleResponse {
        private int emiMonth;
        private double emiAmount;
        private  double principalComponent;
        private double interestComponent;
        private double remainingAmount;
        private String paymentDate;
        private String startDate;
    }
}