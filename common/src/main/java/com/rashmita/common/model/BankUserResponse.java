package com.rashmita.common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BankUserResponse {
    private String email;
    private String mobile;
    private String firstName;
    private String lastName;
    private String gender;
    private String status;
}
