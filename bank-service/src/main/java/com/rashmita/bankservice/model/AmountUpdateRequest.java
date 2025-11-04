package com.rashmita.bankservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AmountUpdateRequest {
    private String accountNumber;
    private String amount;
}
