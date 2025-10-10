package com.rashmita.systemservice.controller;

import com.rashmita.systemservice.constants.ApiConstants;
import com.rashmita.systemservice.exception.NotFoundException;
import com.rashmita.systemservice.model.*;
import com.rashmita.systemservice.service.BankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.rashmita.systemservice.constants.ApiConstants.*;
import static com.rashmita.systemservice.constants.RolesConstants.*;


/**
 * rashmita subedi
 */
@RestController
@RequestMapping(ApiConstants.BANK)
@RequiredArgsConstructor
public class BankController {
    private final BankService bankService;
    @PreAuthorize("hasAuthority('CREATE_BANK')")
    @PostMapping(CREATE)
    public ServerResponse<?> createBank(@Valid @RequestBody BankRequest bankRequest) throws NotFoundException {
        return bankService.createBank(bankRequest);
    }

    @PostMapping(UPDATE)
    @PreAuthorize("hasAuthority('MODIFY_BANK')")
    public ServerResponse<?> updateBank(@Valid @RequestBody BankUpdateRequest bankUpdateRequest) throws NotFoundException {
        return bankService.updateBank(bankUpdateRequest);
    }

    @PostMapping(DELETE)
    @PreAuthorize("hasAuthority('DELETE_BANK')")
    public ServerResponse<?> deleteBank(@Valid @RequestBody BankCodeRequest bankCodeRequest) throws NotFoundException {
        return bankService.deleteBank(bankCodeRequest);
    }

    @GetMapping(GET + BY + ID)
    public ServerResponse<?> getBankById(@Valid @RequestBody BankIdRequest bankIdRequest) throws NotFoundException {
        return bankService.getBankById(bankIdRequest);
    }

    @GetMapping(GET + BY + NAME)
    @PreAuthorize(VIEW_BANK)
    public ServerResponse<?> getByBankCode(@Valid @RequestBody BankCodeRequest bankCodeRequest) throws NotFoundException {
        return bankService.getByBankCode(bankCodeRequest);
    }

    @GetMapping(GET + ALL)
    @PreAuthorize("hasAuthority('VIEW_BANK')")
    public ServerResponse<?> getAllBanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return bankService.getAllBanks(pageable);
    }

    @GetMapping(SORTING + DECENDING + DATE)
    public ServerResponse<?> getAllBanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "establishedDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return bankService.getAllBanks(pageable);
    }
}
