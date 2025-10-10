package com.rashmita.systemservice.entity;
import com.rashmita.common.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="admin")
public class Admin extends AbstractEntity implements UserDetails {

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "middleName")
    private String middleName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Status status;

    @JoinColumn(name = "access_group", nullable = false, referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccessGroup accessGroup;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.accessGroup == null || this.accessGroup.getRoleGroupMaps() == null) {
            return List.of();
        }

        Set<String> permissions = this.accessGroup.getRoleGroupMaps()
                .stream()
                .map(AccessGroupTypeRoleMap::getRole)
                .filter(role -> role != null && role.getPermission() != null)
                .map(Roles::getPermission)
                .collect(Collectors.toSet());

        if (Boolean.TRUE.equals(this.accessGroup.isSuperAdminGroup())) {
            permissions.add("GOD_MODE");
        }

        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
