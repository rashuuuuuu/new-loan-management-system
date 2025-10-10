//package com.rashmita.common.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "bank_user")
//public class BankUser extends AbstractEntity {
//
//    @Column(name="code",nullable = false)
//    private String code;
//
//    @Column(name = "firstname", nullable = false)
//    private String firstName;
//
//    @Column(name = "lastname", nullable = false)
//    private String lastName;
//
//    @Column(name = "password", nullable = false)
//    private String password;
//
//    @Column(name = "email", nullable = false)
//    private String email;
//
//    @Column(name = "mobile", nullable = false)
//    private String mobile;
//
//    @Column(name = "is_active")
//    private boolean isActive;
//
//    @Column(name="gender",nullable = false)
//    private String gender;
//
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "status_id", referencedColumnName = "id")
//    private Status status;
//
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
//    private Roles role;
//
//}
