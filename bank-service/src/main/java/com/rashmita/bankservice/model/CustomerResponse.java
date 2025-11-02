package com.rashmita.bankservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerResponse {
    private String email;
    private String mobile;
    private String firstName;
    private String lastName;
    private String gender;
    private String status;
    private String customerNumber;
    private String accountNumber;
    private String bankCode;
    private Double amount;
}

