package com.rashmita.common.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer")
public class Customer extends AbstractEntity {
    @Column(name="code",nullable = false)
    private String code;

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name="gender",nullable = false)
    private String gender;

    @Column(name="customer_number",nullable = false)
    private String customerNumber;

    @Column(name="account_number",nullable = false)
    private String accountNumber;

    @Column(name="amount",nullable = false)
    private String amount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", referencedColumnName = "id", nullable = false)
    private Bank bank;

}
