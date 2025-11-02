package com.rashmita.commoncommon.repository;

import com.rashmita.commoncommon.entity.TotalAccruedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<TotalAccruedEntity, Long> {
}
