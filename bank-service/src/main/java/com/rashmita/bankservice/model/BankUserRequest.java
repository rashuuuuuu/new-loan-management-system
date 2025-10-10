package com.rashmita.bankservice.model;
import com.rashmita.common.model.AccessGroupDto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BankUserRequest {
    private String email;
    private String password;
    private String mobile;
    private String firstName;
    private String lastName;
    private String gender;
    @NotNull(message = "Access Group Cannot Be Null")
    private AccessGroupDto accessGroup;
}
