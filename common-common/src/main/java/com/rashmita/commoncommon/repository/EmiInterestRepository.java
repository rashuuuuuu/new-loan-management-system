package com.rashmita.commoncommon.repository;

import com.rashmita.commoncommon.entity.EmiInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmiInterestRepository extends JpaRepository<EmiInterest, Long> {
    List<EmiInterest> findByLoanNumber(String loanNumber);
}
