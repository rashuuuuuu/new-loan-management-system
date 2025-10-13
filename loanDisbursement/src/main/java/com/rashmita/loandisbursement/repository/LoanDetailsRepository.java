package com.rashmita.loandisbursement.repository;

import com.rashmita.loandisbursement.entity.LoanDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanDetailsRepository extends JpaRepository<LoanDetails, Long> {
}
