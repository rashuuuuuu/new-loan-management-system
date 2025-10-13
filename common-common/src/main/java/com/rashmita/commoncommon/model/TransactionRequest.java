package com.rashmita.commoncommon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private String loanNumber;
    private String transactionId;
    private List<TransactionDetailRequest> transactions;
}

