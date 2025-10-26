package com.rashmita.commoncommon.repository;

import com.rashmita.commoncommon.entity.DailyJobLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DailyJobLogRepository extends JpaRepository<DailyJobLog, Long> {
    Boolean existsByJobNameAndRunDate(String jobName, LocalDate date);
}
