package com.rashmita.common.model;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccessGroupDetailDto extends ModelBase {
    private String name;
    private String description;
    private List<AccessGroupRoleMapDetailDto> accessGroupRoleMaps;
}
