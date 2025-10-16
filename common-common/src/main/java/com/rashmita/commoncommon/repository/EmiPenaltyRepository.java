package com.rashmita.commoncommon.repository;

import com.rashmita.commoncommon.entity.EmiPenalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmiPenaltyRepository  extends JpaRepository<EmiPenalty, Long> {
    Boolean existsByLoanNumber(String loanNumber);
    List<EmiPenalty> findByLoanNumber(String loanNumber);
}
