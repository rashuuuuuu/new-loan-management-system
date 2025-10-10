package com.rashmita.bankservice.service;

import com.rashmita.bankservice.model.BankUserIdRequest;
import com.rashmita.bankservice.model.BankUserRequest;
import com.rashmita.bankservice.model.BankUserUpdateRequest;
import com.rashmita.common.exception.NotFoundException;
import com.rashmita.common.model.ServerResponse;
import org.springframework.data.domain.Pageable;

public interface BankUserService {
    ServerResponse createBankUser(BankUserRequest bankUserRequestDto) throws NotFoundException;
    ServerResponse updateBankUser(BankUserUpdateRequest bankUserUpdateRequest);
    ServerResponse deleteBankUser(BankUserIdRequest bankUserIdRequest);
    ServerResponse getBankUserById(BankUserIdRequest bankUserIdRequest) throws NotFoundException;
    ServerResponse getAllBankUsers(Pageable pageable);
    ServerResponse blockBankUser(BankUserIdRequest bankUserIdRequest);
    ServerResponse unblockBankUser(BankUserIdRequest bankUserIdRequest);

}
