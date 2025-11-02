package com.rashmita.commoncommon.model;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PrepaymentResponse {
    private String loanNumber;
    private LocalDate prepaymentDate;
}
