package com.rashmita.systemservice.entity;

import com.rashmita.systemservice.constants.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Entity
@Getter
@Setter
@Table(name = "roles")
public class Roles extends AbstractEntity {
    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    @Column(nullable = false)
    private String description;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "permission",unique = true)
    private String permission;

    public String toArray(String[] strings) {
        return String.join(",", strings);
    }
}
