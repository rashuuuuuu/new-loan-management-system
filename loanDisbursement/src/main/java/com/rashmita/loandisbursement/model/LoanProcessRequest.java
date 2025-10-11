package com.rashmita.loandisbursement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanProcessRequest {
    private String accountNumber;
    private String bankCode;
    private String customerNumber;
}
