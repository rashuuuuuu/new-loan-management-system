package com.rashmita.common.model;

import lombok.*;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuperAdminDto {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String mobileNumber;
    private String address;
    private boolean isActive;
    private String gender;
    private boolean isSuperAdmin;
    private AccessGroupDTO accessGroup;
    private StatusDTO status;

    @Data
    public static class AccessGroupDTO {
        private String name;
    }

    @Data
    public static class StatusDTO {
        private String name;
    }
}
