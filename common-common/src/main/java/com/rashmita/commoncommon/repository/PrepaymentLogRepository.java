package com.rashmita.commoncommon.repository;

import com.rashmita.commoncommon.entity.PrepaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrepaymentLogRepository extends JpaRepository<PrepaymentLog, String> {
}
