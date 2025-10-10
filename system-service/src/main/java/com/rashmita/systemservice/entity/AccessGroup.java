package com.rashmita.systemservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "access_group")
public class AccessGroup extends AbstractEntity {
    @Column(name = "name", nullable = false,unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status", referencedColumnName = "id")
    private Status status;

    @Column(name = "is_super_admin_group", nullable = false)
    private boolean isSuperAdminGroup;

    @Column(name = "remarks")
    private String remarks;

    @OneToMany(mappedBy = "accessGroup", fetch = FetchType.EAGER)
    private List<AccessGroupTypeRoleMap> roleGroupMaps;

    @JoinColumn(name = "access_group_type", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccessGroupType accessGroupType;
}