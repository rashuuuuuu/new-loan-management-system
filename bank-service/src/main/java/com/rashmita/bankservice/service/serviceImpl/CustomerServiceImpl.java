package com.rashmita.bankservice.service.serviceImpl;

import com.rashmita.bankservice.mapper.CustomerMapper;
import com.rashmita.bankservice.model.CustomerIdRequest;
import com.rashmita.bankservice.model.CustomerRequest;
import com.rashmita.bankservice.model.CustomerResponse;
import com.rashmita.bankservice.model.CustomerUpdateRequest;
import com.rashmita.bankservice.repository.CustomerRepository;
import com.rashmita.bankservice.service.CustomerService;
import com.rashmita.common.entity.Customer;
import com.rashmita.common.exception.NotFoundException;
import com.rashmita.common.model.PagingResponse;
import com.rashmita.common.model.ResponseUtility;
import com.rashmita.common.model.ServerResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public ServerResponse createCustomer(CustomerRequest customerRequestDto) {
       Customer customer=customerMapper.saveCustomerDetails(customerRequestDto);
        return ResponseUtility.getSuccessfulServerResponse("successful registration with this account number",customer.getAccountNumber());
    }

    @Override
    public ServerResponse updateCustomer(CustomerUpdateRequest customerUpdateRequest) {
       customerMapper.updateCustomerDetails(customerUpdateRequest);
       return ResponseUtility.getSuccessfulServerResponse("Customer Updated Successfully");
    }

    @Override
    public ServerResponse deleteCustomer(CustomerIdRequest customerIdRequest) {
      customerMapper.deleteCustomer(customerIdRequest);
      return ResponseUtility.getSuccessfulServerResponse("Customer Deleted Successfully");
    }

    @Override
    public ServerResponse getCustomerById(CustomerIdRequest customerIdRequest) throws NotFoundException {
    customerMapper.getDetailsById(customerIdRequest);
    return  ResponseUtility.getSuccessfulServerResponse("Customer Details fetched Successfully");
    }

    @Override
    public ServerResponse getAllCustomers(Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        List<CustomerResponse> customerResponseDtos = customerPage.stream()
                .map(Customer -> modelMapper.map(Customer, CustomerResponse.class))
                .collect(Collectors.toList());

        return ResponseUtility.getSuccessfulServerResponse(
                new PagingResponse<>(
                        customerResponseDtos,
                        customerPage.getTotalPages(),
                        customerPage.getTotalElements(),
                        customerPage.getSize(),
                        customerPage.getNumber(),
                        customerPage.isEmpty()
                ), "All Customer Found");
    }

    @Override
    public ServerResponse blockCustomer(CustomerIdRequest customerIdRequest) {
        customerMapper.blockCustomer(customerIdRequest);
        return ResponseUtility.getSuccessfulServerResponse("Customer Blocked Successfully");
    }

    @Override
    public ServerResponse unblockCustomer(CustomerIdRequest customerIdRequest) {
        customerMapper.unBlockCustomer(customerIdRequest);
        return ResponseUtility.getSuccessfulServerResponse("Customer unBlocked Successfully");
    }

    public CustomerResponse getCustomerByBankCodeAndCustomerNumber(String bankCode, String customerNumber) {
        Customer customer = customerRepository.findCustomerByBankCodeAndCustomerNumber(bankCode, customerNumber)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return modelMapper.map(customer, CustomerResponse.class);
    }
}

