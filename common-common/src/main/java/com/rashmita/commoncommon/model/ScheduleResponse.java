package com.rashmita.commoncommon.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ScheduleResponse {
    private String loanNumber;
    private int emiMonths;
    private Double emiAmount;
    private Date paymentDate;
}
