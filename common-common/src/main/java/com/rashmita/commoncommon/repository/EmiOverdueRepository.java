package com.rashmita.commoncommon.repository;

import com.rashmita.commoncommon.entity.EmiOverdue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmiOverdueRepository  extends JpaRepository<EmiOverdue, Long> {
Boolean existsByLoanNumber(String loanNumber);
List<EmiOverdue> getAllByLoanNumber(String loanNumber);

    boolean existsByLoanNumberAndAccrualDate(String loanNumber, LocalDate accrualDate);

    boolean existsByAccrualDate(LocalDate accrualDate);
}
