package com.rashmita.commoncommon.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementRequest {
    private String loanNumber;
    private String accountNumber;
    private Double amount;
    private String transactionId;
    private int emiMonth;
    private List<TransactionDetailRequest> transactions;
}

