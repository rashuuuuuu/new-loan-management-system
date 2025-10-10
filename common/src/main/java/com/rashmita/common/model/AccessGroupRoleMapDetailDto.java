package com.rashmita.common.model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessGroupRoleMapDetailDto extends ModelBase {
    private Boolean isActive;
    private RoleDto roles;
}

