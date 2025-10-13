package com.rashmita.isoservice.repository;

import com.rashmita.isoservice.entity.BankMoney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankMoneyRepository extends JpaRepository<BankMoney,Long> {

    @Query("SELECT b FROM BankMoney b WHERE b.accountNumber = :accountNumber")
    Optional<BankMoney> findByAccountNumber(@Param("accountNumber") String accountNumber);
}
