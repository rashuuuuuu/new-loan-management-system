package com.rashmita.commoncommon.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CreateAccruals {
    private String loanNumber;
    private Long emiId;
    private LocalDate accrualDate;
    private BigDecimal penaltyAmount;
    private BigDecimal interestAmount;
    private BigDecimal lateFeeAmount;
    private BigDecimal overdueAmount;
}
