package com.rashmita.bankservice.controller;

import com.rashmita.bankservice.model.CustomerIdRequest;
import com.rashmita.bankservice.model.CustomerRequest;
import com.rashmita.bankservice.model.CustomerResponse;
import com.rashmita.bankservice.model.CustomerUpdateRequest;
import com.rashmita.bankservice.repository.CustomerRepository;
import com.rashmita.bankservice.service.CustomerService;
import com.rashmita.common.constants.ApiConstants;
import com.rashmita.common.exception.NotFoundException;
import com.rashmita.common.model.ServerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import static com.rashmita.common.constants.ApiConstants.*;

@RestController
@RequestMapping(ApiConstants.CUSTOMER)
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;

    @PostMapping(REGISTER)
    public ServerResponse<?> registerCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        return customerService.createCustomer(customerRequest);
    }

    @PostMapping(UPDATE)
    public ServerResponse<?> updateCustomer(@Valid @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        return customerService.updateCustomer(customerUpdateRequest);
    }

    @PostMapping(DELETE)
    public ServerResponse<?> deleteCustomer(@Valid @RequestBody CustomerIdRequest customerIdRequest) {
        return customerService.deleteCustomer(customerIdRequest);
    }

    @PostMapping(BLOCKED)
    public ServerResponse<?> blockCustomer(@Valid @RequestBody CustomerIdRequest customerIdRequest) {
        return customerService.blockCustomer(customerIdRequest);
    }

    @PostMapping(UNBLOCKED)
    public ServerResponse<?> unBlockCustomer(@Valid @RequestBody CustomerIdRequest customerIdRequest) {
        return customerService.unblockCustomer(customerIdRequest);
    }

    @PostMapping(GET + BY + ID)
    public ServerResponse<?> getCustomerById(@Valid @RequestBody CustomerIdRequest customerIdRequest) throws NotFoundException {
        return customerService.getCustomerById(customerIdRequest);
    }

    @GetMapping("/get/by/code/{bankCode}/{customerNumber}")
    public CustomerResponse getCustomerByBankCodeAndCustomerNumber(
            @PathVariable String bankCode,
            @PathVariable String customerNumber) {
        return customerService.getCustomerByBankCodeAndCustomerNumber(bankCode, customerNumber);
    }

    @GetMapping(GET + ALL)
    public ServerResponse<?> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return customerService.getAllCustomers(pageable);
    }

    @GetMapping(SORTING + DECENDING + DATE)
    public ServerResponse<?> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "establishedDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return customerService.getAllCustomers(pageable);
    }
}

