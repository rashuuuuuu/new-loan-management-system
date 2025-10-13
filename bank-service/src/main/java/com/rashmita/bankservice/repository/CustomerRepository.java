package com.rashmita.bankservice.repository;

import com.rashmita.bankservice.model.CustomerIdRequest;
import com.rashmita.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer getByEmail(String email);
    @Query("SELECT c FROM Customer c WHERE c.bank.bankCode = :bankCode AND c.customerNumber = :customerNumber")
    Optional<Customer> findCustomerByBankCodeAndCustomerNumber(@Param("bankCode") String bankCode,
                                                               @Param("customerNumber") String customerNumber);

    Optional<Customer> getCustomerDetailsById(CustomerIdRequest customerIdRequest);

    @Query("SELECT c FROM Customer c WHERE c.customerNumber = :customerNumber")
    Optional<Customer> findByCustomerNumber(@Param("customerNumber") String customerNumber);
}
