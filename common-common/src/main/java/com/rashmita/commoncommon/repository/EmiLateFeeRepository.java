package com.rashmita.commoncommon.repository;

import com.rashmita.commoncommon.entity.EmiLateFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmiLateFeeRepository  extends JpaRepository<EmiLateFee, Long> {
    Boolean existsEmiLateFeeByLoanNumber(String loanNumber);
   List<EmiLateFee> findByLoanNumber(String loanNumber);
}
