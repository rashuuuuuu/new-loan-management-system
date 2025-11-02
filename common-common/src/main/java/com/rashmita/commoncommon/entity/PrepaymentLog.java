package com.rashmita.commoncommon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prepayment_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrepaymentLog extends AbstractEntity{
private String loanNumber;
private Double amount;
private String type;
private String status;
}
