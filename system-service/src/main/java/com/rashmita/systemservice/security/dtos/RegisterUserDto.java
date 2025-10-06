package com.rashmita.systemservice.security.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RegisterUserDto {
    private String email;

    private String password;

    private String fullName;
}