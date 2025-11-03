package com.rashmita.isoservice.repository;

import com.rashmita.isoservice.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, Long> {
    boolean existsByTransactionId(String transactionId);

    boolean findByLoanNumberAndAccountNumberAndValueDate(String loanNumber, String accountNumber, LocalDate valueDate);
}
