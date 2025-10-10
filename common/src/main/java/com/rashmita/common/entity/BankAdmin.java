package com.rashmita.common.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@Table(name = "bank_admin")
public class BankAdmin extends AbstractEntity {
    @Column(name="name")
    private String name;
    @Column(name="email")
    private String email;
    @Column(name="password")
    private String password;
    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;
    @OneToOne(mappedBy = "bankAdmin")
    private Bank bank;
    @Column(name="username")
    private String username;
}
