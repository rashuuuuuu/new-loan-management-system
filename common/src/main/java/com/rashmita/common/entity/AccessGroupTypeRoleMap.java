package com.rashmita.common.entity;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "access_group_type_role_map")
public class AccessGroupTypeRoleMap extends AbstractEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "access_group_type", referencedColumnName = "id")
    private AccessGroupType accessGroupType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "roles", referencedColumnName = "id")
    private Roles role;

    @ManyToOne(optional = false)
    @JoinColumn(name = "access_group_id", referencedColumnName = "id")
    private AccessGroup accessGroup;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}


