package com.rashmita.systemservice.repository;

import com.rashmita.systemservice.entity.BankUser;
import com.rashmita.systemservice.model.BankUserIdRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankUserRepository extends JpaRepository<BankUser, Long> {

    BankUser getByEmail(String email);

    Optional<BankUser> getBankUserDetailsById(BankUserIdRequest bankUserIdRequest);
}
