package com.rashmita.systemservice.model;

import com.rashmita.common.entity.BankAdmin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BankUpdateRequest {
    private String bankCode;
    private BankAdmin bankAdmin;
    private Boolean isActive;

}