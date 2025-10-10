package com.rashmita.systemservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessGroupDto {
    private String name;
    private String description;
    private Boolean isSuperAdminGroup;
    private StatusDto status;
    private AccessGroupTypeDto accessGroupType;
}