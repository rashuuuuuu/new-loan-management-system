package com.rashmita.commoncommon.repository;

import com.rashmita.commoncommon.entity.TotalPayableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TotalPayableRepository extends JpaRepository<TotalPayableEntity, Long> {
    List<TotalPayableEntity> findByLoanNumber(String loanNumber);

    boolean existsByLoanNumber(String loanNumber);

    Optional<TotalPayableEntity> findByLoanNumberAndTenure(String loanNumber, int i);
}
