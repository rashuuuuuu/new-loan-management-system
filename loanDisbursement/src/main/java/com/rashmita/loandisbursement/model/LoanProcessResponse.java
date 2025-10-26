package com.rashmita.loandisbursement.model;
import com.rashmita.commoncommon.model.CreditScoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanProcessResponse {
        private String transactionToken;
        private CreditScoreResponse creditScoreResponse;
        private String status;
}
