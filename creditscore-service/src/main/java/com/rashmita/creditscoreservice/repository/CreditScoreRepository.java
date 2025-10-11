package com.rashmita.creditscoreservice.repository;

import com.rashmita.creditscoreservice.entity.CreditScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditScoreRepository extends JpaRepository<CreditScore, Long> {
   @Query("SELECT c FROM CreditScore c WHERE c.bankCode = :bankCode AND c.accountNumber = :accountNumber")
   Optional<CreditScore> findCustomerByBankCodeAndAccountNumber(
           @Param("bankCode") String bankCode,
           @Param("accountNumber") String accountNumber);

   List<CreditScore> getCreditScoreByAccountNumber(String creditScoreRequest);
}
