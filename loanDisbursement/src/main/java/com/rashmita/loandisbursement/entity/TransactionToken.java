package com.rashmita.loandisbursement.entity;
import com.rashmita.commoncommon.model.ModelBase;
import com.rashmita.commoncommon.model.Purpose;
import com.rashmita.commoncommon.model.State;
import com.rashmita.commoncommon.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class TransactionToken extends ModelBase {

    private String token;

    private Map<String, Object> claims;

    private String customerNumber;

    private String bankCode;

    private Purpose purpose;

    private State state;

    private Status status;

    private Date createdAt;

    private Date updatedAt;

    private Date expiresAt;
}