package com.rashmita.bankservice.repository;

import com.rashmita.common.entity.LoanConfiguration;

import com.rashmita.bankservice.model.LoanConfigIdRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanConfigurationRepository extends JpaRepository<LoanConfiguration, Long>{

    LoanConfiguration  getLoanConfigurationById(LoanConfigIdRequest loanConfigIdRequest);
}
