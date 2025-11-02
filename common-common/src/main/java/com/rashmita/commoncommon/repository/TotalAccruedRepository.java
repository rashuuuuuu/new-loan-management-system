package com.rashmita.commoncommon.repository;

import com.rashmita.commoncommon.entity.TotalAccruedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TotalAccruedRepository extends JpaRepository<TotalAccruedEntity, Long> {
List<TotalAccruedEntity> findByLoanNumber(String loanNumber);

    boolean existsByLoanNumber(String loanNumber);
}
