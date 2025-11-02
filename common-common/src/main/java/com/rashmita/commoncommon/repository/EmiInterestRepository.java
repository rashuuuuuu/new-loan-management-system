package com.rashmita.commoncommon.repository;

import com.rashmita.commoncommon.entity.EmiInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmiInterestRepository extends JpaRepository<EmiInterest, Long> {
    List<EmiInterest> findByLoanNumber(String loanNumber);

    boolean existsByLoanNumberAndAccrualDate(String loanNumber, LocalDate today);

    boolean existsByAccrualDate(LocalDate accrualDate);
    boolean existsByEmiIdAndAccrualDate(Long emiId, LocalDate accrualDate);

    boolean existsByLoanNumberAndAccrualDateAndEmiMonth(String loanNumber, LocalDate date, int emiId);

    EmiInterest findTopByLoanNumberAndEmiIdOrderByAccrualDateDesc(String loanNumber, Long emiId);

    Optional<EmiInterest> findByLoanNumberAndAccrualDateAndEmiMonth(String loanNumber, LocalDate today, int emiMonth);
}
