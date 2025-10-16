package com.rashmita.commoncommon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTotalPayable {
    private Double totalOverdue;
    private Double totalLateFee;
    private Double totalPenalty;
    private Double emiInstallmentAmount;
}
