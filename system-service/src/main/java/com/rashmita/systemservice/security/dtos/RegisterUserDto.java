package com.rashmita.systemservice.security.dtos;

import com.rashmita.systemservice.model.AccessGroupModel;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Access Group Cannot Be Null")
    private AccessGroupModel accessGroup;
}