package com.rashmita.commoncommon.repository;

import com.rashmita.commoncommon.entity.LoanDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanDetailsRepository extends JpaRepository<LoanDetails, Long> {
    List<LoanDetails> findByStatusIn(List<String> active);
    @Query("SELECT l FROM LoanDetails l")
    List<LoanDetails> getAllLoanDetails();
}
