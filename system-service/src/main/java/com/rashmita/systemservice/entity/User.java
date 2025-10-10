package com.rashmita.systemservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Table(name = "users")
@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name="fullname")
    private String fullName;

    @Column(name="username")
    private String username;

    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Status status;

    @JoinColumn(name = "access_group", nullable = false, referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccessGroup accessGroup;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (accessGroup != null && accessGroup.getRoleGroupMaps() != null) {
            Hibernate.initialize(accessGroup.getRoleGroupMaps());
            return accessGroup.getRoleGroupMaps().stream()
                    .filter(AccessGroupTypeRoleMap::getIsActive)
                    .map(AccessGroupTypeRoleMap::getRole)
                    .map(role -> new SimpleGrantedAuthority(role.getPermission()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
