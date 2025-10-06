package com.rashmita.systemservice.repository;

import com.rashmita.systemservice.entity.Bank;
import com.rashmita.systemservice.model.BankIdRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    boolean existsByBankCode(String bankCode);
//
//    Bank getByBankName(String bankName);

    Optional<Bank> getBankDetailsById(BankIdRequest bankIdRequest);
    Optional<Bank>  findById(BankIdRequest bankIdRequest);
    Optional<Bank>  findByBankCode(String bankCodeRequest);

//    Optional<Bank> getBankDetailsByBankNameIgnoreCase(String nameRequest);

    Bank getByBankCode(String bankCode);
}
