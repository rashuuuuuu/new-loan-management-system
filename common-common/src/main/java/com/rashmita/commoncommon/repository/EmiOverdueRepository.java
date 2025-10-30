package com.rashmita.commoncommon.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.rashmita.commoncommon.entity.EmiOverdue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmiOverdueRepository  extends JpaRepository<EmiOverdue, Long> {
Boolean existsByLoanNumber(String loanNumber);
List<EmiOverdue> getAllByLoanNumber(String loanNumber);

    boolean existsByLoanNumberAndAccrualDate(String loanNumber, LocalDate accrualDate);

    boolean existsByAccrualDate(LocalDate accrualDate);

    boolean existsByEmiIdAndAccrualDate(Long emiId, LocalDate today);
    boolean existsByLoanNumberAndAccrualDateAndEmiMonth(String loanNumber, LocalDate date, int emiId);
    EmiOverdue findTopByLoanNumberAndEmiIdOrderByAccrualDateDesc(String loanNumber, Long emiId);
}
