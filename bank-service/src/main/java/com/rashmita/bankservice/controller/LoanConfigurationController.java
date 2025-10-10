package com.rashmita.bankservice.controller;
import com.rashmita.bankservice.model.LoanConfigIdRequest;
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
    @PreAuthorize("hasAuthority('CREATE_LOAN_CONFIGURATION')")
    public ServerResponse<?> createLoanConfig(@Valid @RequestBody LoanConfigurationRequest loanConfigurationRequest) {
        return loanConfigurationService.createLoanConfig(loanConfigurationRequest);
    }
    @PostMapping(UPDATE)
    @PreAuthorize("hasAuthority('MODIFY_LOAN_CONFIGURATION')")
    public ServerResponse<?> updateCustomer(@Valid @RequestBody LoanUpdateRequest loanUpdateRequest) {
        return loanConfigurationService.updateLoanConfig(loanUpdateRequest);
    }

    @PostMapping(DELETE)
    @PreAuthorize("hasAuthority('DELETE_LOAN_CONFIGURATION')")
    public ServerResponse<?> deleteCustomer(@Valid @RequestBody LoanConfigIdRequest loanConfigIdRequest) {
        return loanConfigurationService.deleteLoanConfig(loanConfigIdRequest);
    }

    @GetMapping(GET+BY+ID)
    @PreAuthorize("hasAuthority('VIEW_LOAN_CONFIGURATION')")
    public ServerResponse<?> getCustomerById(@Valid @RequestParam LoanConfigIdRequest loanConfigIdRequest) throws NotFoundException {
        return loanConfigurationService.getLoanConfigById(loanConfigIdRequest);
    }

}
