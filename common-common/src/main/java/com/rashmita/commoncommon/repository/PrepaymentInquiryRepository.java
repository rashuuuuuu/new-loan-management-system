package com.rashmita.commoncommon.repository;

import com.rashmita.commoncommon.entity.PrepaymentInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrepaymentInquiryRepository extends JpaRepository<PrepaymentInquiry,String> {
    boolean existsByLoanNumber(String loanNumber);

    Optional<PrepaymentInquiry> findByLoanNumber(String loanNumber);
}
