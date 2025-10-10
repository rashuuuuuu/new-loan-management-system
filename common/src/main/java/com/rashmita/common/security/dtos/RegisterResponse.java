package com.rashmita.common.security.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private Long id;
    private String fullName;
    private String email;
    private String accessGroup;
    private Date createdAt;

}
