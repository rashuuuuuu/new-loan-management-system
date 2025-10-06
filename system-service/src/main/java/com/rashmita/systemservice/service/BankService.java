package com.rashmita.systemservice.service;

import com.rashmita.systemservice.exception.NotFoundException;
import com.rashmita.systemservice.model.*;

import org.springframework.data.domain.Pageable;

public interface BankService {
    ServerResponse createBank(BankRequest bankRequestDto);
    ServerResponse updateBank(BankUpdateRequest bankUpdateRequest);
    ServerResponse deleteBank(BankCodeRequest bankCodeRequest);
    ServerResponse getBankById(BankIdRequest bankIdRequest) throws NotFoundException;
    ServerResponse getAllBanks(Pageable pageable);
    ServerResponse getByBankCode(BankCodeRequest bankCodeRequest) throws NotFoundException;
}
