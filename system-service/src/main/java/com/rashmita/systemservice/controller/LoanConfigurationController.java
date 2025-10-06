//package com.rashmita.systemservice.controller;
//
//import com.rashmita.systemservice.constants.ApiConstants;
//import com.rashmita.systemservice.exception.NotFoundException;
//import com.rashmita.systemservice.model.ServerResponse;
//import com.rashmita.systemservice.service.LoanConfigurationService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import static com.rashmita.systemservice.constants.ApiConstants.*;
//
//@RestController
//@RequestMapping(ApiConstants.LOANCONFIGURATION)
//@PreAuthorize("hasAnyRole('LOANCONFIGMANAGER')")
//@RequiredArgsConstructor
//public class LoanConfigurationController {
//    private final LoanConfigurationService loanConfigurationService;
//    @PostMapping(CREATE)
//    public ServerResponse<?> createLoanConfig(@Valid @RequestBody LoanConfigurationRequest loanConfigurationRequest) {
//        return loanConfigurationService.createLoanConfig(loanConfigurationRequest);
//    }
//    @PostMapping(UPDATE)
//    public ServerResponse<?> updateCustomer(@Valid @RequestBody LoanUpdateRequest loanUpdateRequest) {
//        return loanConfigurationService.updateLoanConfig(loanUpdateRequest);
//    }
//
//    @PostMapping(DELETE)
//    public ServerResponse<?> deleteCustomer(@Valid @RequestBody LoanConfigIdRequest loanConfigIdRequest) {
//        return loanConfigurationService.deleteLoanConfig(loanConfigIdRequest);
//    }
//
//    @GetMapping(GET+BY+ID)
//    public ServerResponse<?> getCustomerById(@Valid @RequestParam LoanConfigIdRequest loanConfigIdRequest) throws NotFoundException {
//        return loanConfigurationService.getLoanConfigById(loanConfigIdRequest);
//    }
//    //to do get all configuration api
//
//}
