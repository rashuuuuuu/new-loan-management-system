package com.rashmita.bankservice.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerUpdateRequest {
    private String email;
    private String mobile;
    private  String firstName;
    private  String lastName;
    private  String gender;

}
