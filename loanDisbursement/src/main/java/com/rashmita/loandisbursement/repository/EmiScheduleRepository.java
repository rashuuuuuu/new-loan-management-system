package com.rashmita.loandisbursement.repository;

import com.rashmita.loandisbursement.entity.EmiSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmiScheduleRepository extends JpaRepository<EmiSchedule, Long> {

}
