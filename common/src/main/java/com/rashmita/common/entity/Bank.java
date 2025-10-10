package com.rashmita.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Entity
@Getter
@Setter
@Table(name = "bank")
public class Bank extends AbstractEntity {
    @Column(name = "address")
    private String address;
    @Column(name = "bank_code", unique = true, nullable = false)
    private String bankCode;
    @Column(name = "established_date")
    private Date establishedDate;
    @Column(name = "isActive")
    private Boolean isActive;
    @JoinColumn(name = "bank_admin", referencedColumnName = "id", nullable = false)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BankAdmin bankAdmin;
    @Column(name = "created_date")
    private String createdDate;
    @Column(name = "updated_date")
    private String updatedDate;
    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;
    @OneToOne(mappedBy = "bank", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private LoanConfiguration loanConfiguration;
}
