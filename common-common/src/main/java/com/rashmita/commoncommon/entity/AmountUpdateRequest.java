package com.rashmita.commoncommon.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AmountUpdateRequest {
    private String accountNumber;
    private Double amount;
}
