package com.rashmita.systemservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "bank_admin")
public class BankAdmin extends AbstractEntity {

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;

    @OneToOne(mappedBy = "bankAdmin")
    private Bank bank;

    @Column(name = "username")
    private String username;

    @ManyToOne(optional = false)
    @JoinColumn(name = "access_group", referencedColumnName = "id", nullable = false)
    private AccessGroup accessGroup;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // Initialize roles if lazy loaded
//        Hibernate.initialize(this.accessGroup.getRoleGroupMaps());
//        return this.accessGroup.getRoleGroupMaps().stream()
//                .filter(AccessGroupTypeRoleMap::getIsActive)
//                .map(AccessGroupTypeRoleMap::getRole)
//                .map(role -> new SimpleGrantedAuthority(role.getPermission()))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true; // You can customize based on your 'status' entity if needed
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true; // Customize if you have a locked field
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
}
