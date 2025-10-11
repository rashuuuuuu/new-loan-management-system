package com.rashmita.bankservice.service;

import com.rashmita.bankservice.model.CustomerIdRequest;
import com.rashmita.bankservice.model.CustomerRequest;
import com.rashmita.bankservice.model.CustomerResponse;
import com.rashmita.bankservice.model.CustomerUpdateRequest;
import com.rashmita.common.exception.NotFoundException;
import com.rashmita.common.model.ServerResponse;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    ServerResponse createCustomer(CustomerRequest customerRequestDto);
    ServerResponse updateCustomer(CustomerUpdateRequest customerUpdateRequest);
    ServerResponse deleteCustomer(CustomerIdRequest customerIdRequest);
    ServerResponse getCustomerById(CustomerIdRequest customerIdRequest) throws NotFoundException;
    ServerResponse getAllCustomers(Pageable pageable);
    ServerResponse blockCustomer(CustomerIdRequest customerIdRequest);
    ServerResponse unblockCustomer(CustomerIdRequest customerIdRequest);
    CustomerResponse getCustomerByBankCodeAndCustomerNumber(String bankCode, String customerNumber);

}
