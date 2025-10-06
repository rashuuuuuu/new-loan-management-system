package com.rashmita.systemservice.controller;

import com.rashmita.systemservice.constants.ApiConstants;
import com.rashmita.systemservice.exception.NotFoundException;
import com.rashmita.systemservice.model.BankUserIdRequest;
import com.rashmita.systemservice.model.BankUserRequest;
import com.rashmita.systemservice.model.BankUserUpdateRequest;
import com.rashmita.systemservice.model.ServerResponse;
import com.rashmita.systemservice.service.BankUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static com.rashmita.systemservice.constants.ApiConstants.*;

@RestController
@RequestMapping(ApiConstants.BANKUSER)
@PreAuthorize("hasAnyRole('BANKADMIN')")
@RequiredArgsConstructor
public class BankUserController {
    private final BankUserService bankUserService;
    @PostMapping(CREATE)
    public ServerResponse<?> createBankUser(@Valid @RequestBody BankUserRequest bankUserRequest) {
        return bankUserService.createBankUser(bankUserRequest);
    }
    @PostMapping(UPDATE)
    public ServerResponse<?> updateBankUser(@Valid @RequestBody BankUserUpdateRequest bankUserUpdateRequest) {
        return bankUserService.updateBankUser(bankUserUpdateRequest);
    }

    @PostMapping(DELETE)
    public ServerResponse<?> deleteBankUser(@Valid @RequestBody BankUserIdRequest bankUserIdRequest) {
        return bankUserService.deleteBankUser(bankUserIdRequest);
    }
@PostMapping(BLOCKED)
    public ServerResponse<?> blockBankUser(@Valid @RequestBody BankUserIdRequest bankUserIdRequest) {
        return bankUserService.blockBankUser(bankUserIdRequest);
}
@PostMapping(UNBLOCKED)
public  ServerResponse<?> unBlockBankUser(@Valid @RequestBody BankUserIdRequest bankUserIdRequest) {
        return bankUserService.unblockBankUser(bankUserIdRequest);
}

    @GetMapping(GET+BY+ID)
    public ServerResponse<?> getBankUserById(@Valid @RequestBody BankUserIdRequest customerIdRequest) throws NotFoundException {
        return bankUserService.getBankUserById(customerIdRequest);
    }

    @GetMapping(GET+ALL)
    public ServerResponse<?> getAllBankUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return bankUserService.getAllBankUsers(pageable);
    }
    @GetMapping(SORTING+DECENDING+DATE)
    public ServerResponse<?> getAllBankUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "establishedDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return bankUserService.getAllBankUsers(pageable);
    }
}

