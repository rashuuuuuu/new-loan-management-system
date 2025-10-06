package com.rashmita.systemservice.service;


import com.rashmita.systemservice.exception.NotFoundException;
import com.rashmita.systemservice.model.BankUserIdRequest;
import com.rashmita.systemservice.model.BankUserRequest;
import com.rashmita.systemservice.model.BankUserUpdateRequest;
import com.rashmita.systemservice.model.ServerResponse;

import org.springframework.data.domain.Pageable;

public interface BankUserService {
    ServerResponse createBankUser(BankUserRequest bankUserRequestDto);
    ServerResponse updateBankUser(BankUserUpdateRequest bankUserUpdateRequest);
    ServerResponse deleteBankUser(BankUserIdRequest bankUserIdRequest);
    ServerResponse getBankUserById(BankUserIdRequest bankUserIdRequest) throws NotFoundException;
    ServerResponse getAllBankUsers(Pageable pageable);
    ServerResponse blockBankUser(BankUserIdRequest bankUserIdRequest);
    ServerResponse unblockBankUser(BankUserIdRequest bankUserIdRequest);

}
