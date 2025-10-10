package com.rashmita.bankservice.repository;

import com.rashmita.bankservice.entity.BankUser;
import com.rashmita.bankservice.model.BankUserIdRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankUserRepository extends JpaRepository<BankUser, Long> {

    Optional<BankUser> getByEmail(String email);
    BankUser findByEmail(String email);

    Optional<BankUser> getBankUserDetailsById(BankUserIdRequest bankUserIdRequest);

    BankUser getBankUsersByEmail(String email);
}

