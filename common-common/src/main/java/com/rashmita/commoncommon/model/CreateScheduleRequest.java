package com.rashmita.commoncommon.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateScheduleRequest {
    private int emiMonths;
    private Double emiAmount;
    private String loanNumber;
    private Date paymentDate;
}
