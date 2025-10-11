package com.rashmita.creditscoreservice.controller;
import com.rashmita.commoncommon.exception.NotFoundException;
import com.rashmita.commoncommon.model.ServerResponse;
import com.rashmita.creditscoreservice.model.CreditScoreRequest;
import com.rashmita.creditscoreservice.service.CreditScoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.rashmita.commoncommon.constants.ApiConstants.*;
@RestController
@RequestMapping(CREDITSCORE)
@RequiredArgsConstructor
public class CreditScoreController {
    private final CreditScoreService creditScoreService;
    @GetMapping(GET+BY+ACCOUNT+NUMBER)
    public ServerResponse<?> getCreditScoreByAccountNumber(@Valid @RequestBody CreditScoreRequest creditScoreRequest) throws NotFoundException {
        return creditScoreService.getCreditScoreByAccountNumber(creditScoreRequest);
    }
    @GetMapping("/get/by/{bankCode}/{customerNumber}")
    public ServerResponse<?> getCustomerByBankCodeAndAccountNumber(
            @PathVariable String bankCode,
            @PathVariable String customerNumber) throws NotFoundException {
        return creditScoreService.getCustomerByBankCodeAndAccountNumber(bankCode, customerNumber);
    }

}
