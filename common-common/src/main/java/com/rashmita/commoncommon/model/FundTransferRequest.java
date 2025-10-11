package com.rashmita.commoncommon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FundTransferRequest {
    private String loanNumber;
    private String uniqueTransactionId;
    private List<TransactionDetail> transactions;
}
