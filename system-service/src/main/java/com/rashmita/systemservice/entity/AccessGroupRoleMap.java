package com.rashmita.systemservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@Table(name="access_group_role_map")
public class AccessGroupRoleMap extends AbstractEntity {
    @JoinColumn(name = "access_group", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccessGroup accessGroup;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @JoinColumn(name = "roles", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Roles roles;
}
