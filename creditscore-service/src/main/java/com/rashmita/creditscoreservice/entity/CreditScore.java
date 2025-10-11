package com.rashmita.creditscoreservice.entity;
import com.rashmita.commoncommon.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Table(name = "foneloan_limit")
@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class CreditScore extends AbstractEntity {
    @Column(name="bankCode",nullable = false)
    private String bankCode;

    @Column(name="account_number",nullable=false)
    private String accountNumber;

    @Column(name="mobile_number",nullable = false)
    private String mobileNumber;

    @Column(name="one_month_limit")
    private Double oneMonthLimit;

    @Column(name="emi_month",nullable = false)
    private int emiMonth;

    @Column(name="emi_max_amount",nullable = false)
    private Double emiMaxAmount;

}
