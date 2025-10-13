package com.rashmita.commoncommon.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankIdAndCustomerRequest {
    private String bankCode;
    private String customerNumber;
}
