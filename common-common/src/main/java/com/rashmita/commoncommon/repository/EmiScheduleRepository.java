package com.rashmita.commoncommon.repository;

import com.rashmita.commoncommon.entity.EmiSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmiScheduleRepository extends JpaRepository<EmiSchedule, Long> {
//    List<EmiSchedule> findUnpaidByLoanNumber(String id);

    List<EmiSchedule> findEmiScheduleByLoanNumber(String loanNumber);

    boolean existsEmiScheduleByLoanNumber(String loanNumber);

    List<EmiSchedule> findByLoanNumber(String loanNumber);
}
