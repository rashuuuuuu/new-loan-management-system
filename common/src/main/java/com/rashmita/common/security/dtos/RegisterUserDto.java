package com.rashmita.common.security.dtos;

import com.rashmita.common.model.CreateAccessGroupModel;
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
    private CreateAccessGroupModel accessGroup;
}