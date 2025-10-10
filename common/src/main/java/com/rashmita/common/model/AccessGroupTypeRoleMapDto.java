package com.rashmita.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessGroupTypeRoleMapDto {
    private String accessGroupTypeName;
    private String accessGroupName;
    private String roleName;
    private Boolean isActive;
}