package com.rashmita.common.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BankResponse {
    private String statusId;
    private String bankCode;
    private String establishedDate;
}
