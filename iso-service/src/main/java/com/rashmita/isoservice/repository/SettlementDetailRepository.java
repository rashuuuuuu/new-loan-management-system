package com.rashmita.isoservice.repository;

import com.rashmita.isoservice.entity.SettlementDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementDetailRepository extends JpaRepository<SettlementDetail, Long> {
    boolean existsByTransactionId(String transactionId);
}
