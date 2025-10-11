package com.rashmita.bankservice.mapper;

import com.rashmita.bankservice.model.CustomerIdRequest;
import com.rashmita.bankservice.model.CustomerRequest;
import com.rashmita.bankservice.model.CustomerResponse;
import com.rashmita.bankservice.model.CustomerUpdateRequest;
import com.rashmita.bankservice.repository.CustomerRepository;
import com.rashmita.common.constants.StatusConstants;
import com.rashmita.common.entity.Customer;
import com.rashmita.common.entity.Status;
import com.rashmita.common.exception.NotFoundException;
import com.rashmita.common.repository.BankRepository;
import com.rashmita.common.repository.StatusRepository;
import com.rashmita.common.util.RandomNumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerMapper {
    /**
     * rashmita subedi
     */
    @Autowired
    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;
    private final StatusRepository statusRepository;
    private final BankRepository bankRepository;

    public Customer saveCustomerDetails(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setCode(UUID.randomUUID().toString());
        customer.setPassword(customerRequest.getPassword());
        customer.setUsername(customerRequest.getUserName());
        customer.setEmail(customerRequest.getEmail());
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setGender(customerRequest.getGender());
        customer.setBank(bankRepository.getByBankCode(customerRequest.getBankCode()));
        customer.setCustomerNumber(customerRequest.getCustomerNumber());
        customer.setAccountNumber(RandomNumberGenerator.generate10DigitRandomNumber());
        customer.setStatus(statusRepository.getStatusByName(StatusConstants.CREATED.getName()));
        return customerRepository.save(customer);
    }

    public Customer updateCustomerDetails(CustomerUpdateRequest customerUpdateRequest) {

        String email = customerUpdateRequest.getEmail();
        Customer customer = customerRepository.getByEmail(email);
        if (customerUpdateRequest.getEmail() != null) {
            customer.setFirstName(customerUpdateRequest.getFirstName());
            customer.setLastName(customerUpdateRequest.getLastName());
            customer.setGender(customerUpdateRequest.getGender());
            customer.setStatus(statusRepository.getStatusByName(StatusConstants.UPDATED.getName()));
        }
        return customerRepository.save(customer);
    }

    /** Get details by ID **/
    public CustomerResponse getDetailsById(CustomerIdRequest customerIdRequest) throws NotFoundException {
        Customer customer = customerRepository.findById(customerIdRequest.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        Status deletedStatus = statusRepository.getStatusByName(StatusConstants.DELETED.getName());
        if (customer.getStatus() == null ||
                customer.getStatus().getName().equalsIgnoreCase(deletedStatus.getName())) {
            throw new NotFoundException("Customer is inactive or deleted");
        }

        return modelMapper.map(customer, CustomerResponse.class);
    }
    public void deleteCustomer(CustomerIdRequest customerIdRequest) {
        Optional<Customer> customerOptional = customerRepository.findById(customerIdRequest.getCustomerId());

        if (customerOptional.isPresent()) {
            Customer foundCustomer = customerOptional.get();
            foundCustomer.setStatus(statusRepository.getStatusByName(StatusConstants.DELETED.getName()));
            customerRepository.save(foundCustomer);
        } else {
            System.out.println("customer does not exist");
        }
    }

    public void blockCustomer(CustomerIdRequest customerIdRequest) {
        Optional<Customer> customerOptional = customerRepository.findById(customerIdRequest.getCustomerId());
        if (customerOptional.isPresent()) {
            if (customerOptional.get().getStatus() != statusRepository.getStatusByName(StatusConstants.DELETED.getName())) {
                Customer foundCustomer = customerOptional.get();
                foundCustomer.setStatus(statusRepository.getStatusByName(StatusConstants.BLOCKED.getName()));
            }
        } else {
            System.out.println("customer does not exist");
        }
    }

    public void unBlockCustomer(CustomerIdRequest customerIdRequest) {
        Optional<Customer> customerOptional = customerRepository.findById(customerIdRequest.getCustomerId());
        if (customerOptional.isPresent()) {
            if (customerOptional.get().getStatus() == statusRepository.getStatusByName(StatusConstants.BLOCKED.getName())) {
                Customer foundCustomer = customerOptional.get();
                foundCustomer.setStatus(statusRepository.getStatusByName(StatusConstants.UNBLOCKED.getName()));
            }
        } else {
            System.out.println("Only blocked Customer can be unblocked");
        }
    }
}
