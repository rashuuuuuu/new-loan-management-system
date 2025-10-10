package com.rashmita.systemservice.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "roles")
public class Roles extends AbstractEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @JoinColumn(name = "parent_role", referencedColumnName = "id")
    @ManyToOne
    private Roles parentRole;

    @Column(name = "parent_name")
    private String parentName;

    @Column(name = "permission")
    private String permission;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AccessGroupTypeRoleMap> groupTypeMappings;

}