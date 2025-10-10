//package com.rashmita.systemservice.entity;
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.Getter;
//import lombok.Setter;
//
//@Data
//@Entity
//@Getter
//@Setter
//@Table(name="system_user")
//public class SystemUser  extends AbstractEntity {
//    @Column(name="name")
//    private String name;
//
//    @Column(name="email")
//    private String email;
//
//    @Column(name="password")
//    private String password;
//
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "status_id", referencedColumnName = "id")
//    private Status status;
//
//    @Column(name="username")
//    private String username;
//
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
//    private AccessGroup userGroup;
//}
