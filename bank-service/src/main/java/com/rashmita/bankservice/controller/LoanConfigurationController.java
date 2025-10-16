package com.rashmita.bankservice.controller;
import com.rashmita.bankservice.model.LoanConfigBankCodeRequest;
import com.rashmita.bankservice.model.LoanConfigurationRequest;
import com.rashmita.bankservice.model.LoanUpdateRequest;
import com.rashmita.bankservice.service.LoanConfigurationService;
import com.rashmita.common.constants.ApiConstants;
import com.rashmita.common.exception.NotFoundException;
import com.rashmita.common.model.ServerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static com.rashmita.common.constants.ApiConstants.*;
@RestController
@RequestMapping(ApiConstants.LOANCONFIGURATION)
@RequiredArgsConstructor
public class LoanConfigurationController {
    private final LoanConfigurationService loanConfigurationService;
    @PostMapping(CREATE)
//    @PreAuthorize("hasAuthority('CREATE_LOAN_CONFIGURATION')")
    public ServerResponse<?> createLoanConfig(@Valid @RequestBody LoanConfigurationRequest loanConfigurationRequest) {
        return loanConfigurationService.createLoanConfig(loanConfigurationRequest);
    }
    @PostMapping(UPDATE)
//    @PreAuthorize("hasAuthority('MODIFY_LOAN_CONFIGURATION')")
    public ServerResponse<?> updateLoanConfig(@Valid @RequestBody LoanUpdateRequest loanUpdateRequest) {
        return loanConfigurationService.updateLoanConfig(loanUpdateRequest);
    }

    @PostMapping(DELETE)
//    @PreAuthorize("hasAuthority('DELETE_LOAN_CONFIGURATION')")
    public ServerResponse<?> deleteLoanConfig(@Valid @RequestBody LoanConfigBankCodeRequest loanConfigBankCodeRequest) {
        return loanConfigurationService.deleteLoanConfig(loanConfigBankCodeRequest);
    }

    @PostMapping("/get/by/code")
    public ServerResponse<?> getLoanConfigurationByBankCode(
            @RequestBody LoanConfigBankCodeRequest request) throws NotFoundException {
        return loanConfigurationService.getLoanConfigByBankCode(request);
    }

}
