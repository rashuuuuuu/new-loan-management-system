package com.rashmita.bankservice.model;
import lombok.*;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;
    private String userName;
    private String customerNumber;
    private String bankCode;
}
