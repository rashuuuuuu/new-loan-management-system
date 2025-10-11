package com.rashmita.loandisbursement.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanProcessResponse {
    private String bankCode;
    private Double oneMonthLimit;
    private List<Map<String, Object>> tenures;
    private Data data;
    @lombok.Data
    public static class Data {
        private String transactionToken;
        private double minAmount;
        private double maxAmount;
    }

}
