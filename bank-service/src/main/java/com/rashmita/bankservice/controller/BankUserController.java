package com.rashmita.bankservice.controller;
import com.rashmita.bankservice.model.BankUserIdRequest;
import com.rashmita.bankservice.model.BankUserRequest;
import com.rashmita.bankservice.model.BankUserUpdateRequest;
import com.rashmita.bankservice.service.BankUserService;
import com.rashmita.common.constants.ApiConstants;
import com.rashmita.common.exception.NotFoundException;
import com.rashmita.common.model.ServerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static com.rashmita.common.constants.ApiConstants.*;

@RestController
@RequestMapping(ApiConstants.BANKUSER)
@RequiredArgsConstructor
public class BankUserController {
    private final BankUserService bankUserService;
    @PostMapping(CREATE)
    @PreAuthorize("hasAuthority('BANK_USER')")
    public ServerResponse<?> createBankUser(@Valid @RequestBody BankUserRequest bankUserRequest) throws NotFoundException {
        return bankUserService.createBankUser(bankUserRequest);
    }
    @PostMapping(UPDATE)
    @PreAuthorize("hasAuthority('BANK_USER')")
    public ServerResponse<?> updateBankUser(@Valid @RequestBody BankUserUpdateRequest bankUserUpdateRequest) {
        return bankUserService.updateBankUser(bankUserUpdateRequest);
    }

    @PostMapping(DELETE)
    @PreAuthorize("hasAuthority('BANK_USER')")
    public ServerResponse<?> deleteBankUser(@Valid @RequestBody BankUserIdRequest bankUserIdRequest) {
        return bankUserService.deleteBankUser(bankUserIdRequest);
    }
@PostMapping(BLOCKED)
@PreAuthorize("hasAuthority('BANK_USER')")
    public ServerResponse<?> blockBankUser(@Valid @RequestBody BankUserIdRequest bankUserIdRequest) {
        return bankUserService.blockBankUser(bankUserIdRequest);
}
@PostMapping(UNBLOCKED)
@PreAuthorize("hasAuthority('BANK_USER')")
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
    @PreAuthorize("hasAuthority('BANK_USER')")
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

