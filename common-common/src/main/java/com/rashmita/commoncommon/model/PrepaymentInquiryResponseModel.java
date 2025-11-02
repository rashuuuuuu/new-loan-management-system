package com.rashmita.commoncommon.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PrepaymentInquiryResponseModel {
    private String loanNumber;
    private Double amount;
    private LocalDate prepaymentDate;
}
