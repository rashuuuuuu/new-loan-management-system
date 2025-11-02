package com.rashmita.commoncommon.repository;

import com.rashmita.commoncommon.entity.EmiInterest;
import com.rashmita.commoncommon.entity.EmiPenalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmiPenaltyRepository extends JpaRepository<EmiPenalty, Long> {
    Boolean existsByLoanNumber(String loanNumber);

    List<EmiPenalty> findByLoanNumber(String loanNumber);

    boolean existsByLoanNumberAndAccrualDate(String loanNumber, LocalDate accrualDate);

    boolean existsByAccrualDate(LocalDate accrualDate);

    boolean existsByEmiIdAndAccrualDate(Long emiId, LocalDate today);

    boolean existsByLoanNumberAndAccrualDateAndEmiMonth(String loanNumber, LocalDate date, int emiId);
    EmiPenalty findTopByLoanNumberAndEmiIdOrderByAccrualDateDesc(String loanNumber, Long emiId);

    Optional<EmiPenalty> findByLoanNumberAndAccrualDateAndEmiMonth(String loanNumber, LocalDate today, int emiMonth);

}
