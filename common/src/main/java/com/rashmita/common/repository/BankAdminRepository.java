package com.rashmita.common.repository;

import com.rashmita.common.entity.Bank;
import com.rashmita.common.entity.BankAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAdminRepository extends JpaRepository<BankAdmin, Long> {

    Optional<BankAdmin> findByBank(Bank bank);
}
