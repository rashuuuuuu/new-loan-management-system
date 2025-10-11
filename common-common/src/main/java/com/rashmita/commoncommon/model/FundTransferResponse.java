package com.rashmita.commoncommon.model;

import lombok.*;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundTransferResponse {
    private String status;
    private String message;
    private String uniqueTransactionId;
    private String loanNumber;
    private Double totalDebit;
    private Double totalCredit;
    private LocalDateTime processedAt;
    private String referenceId;
}
