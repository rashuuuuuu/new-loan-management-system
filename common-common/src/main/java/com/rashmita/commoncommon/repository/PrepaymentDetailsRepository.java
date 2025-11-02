package com.rashmita.commoncommon.repository;

import com.rashmita.commoncommon.entity.PrepaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrepaymentDetailsRepository extends JpaRepository<PrepaymentDetails, String> {
}
