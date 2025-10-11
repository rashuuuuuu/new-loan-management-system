package com.rashmita.bankservice.repository;

import com.rashmita.common.entity.LoanConfiguration;

import com.rashmita.bankservice.model.LoanConfigBankCodeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LoanConfigurationRepository extends JpaRepository<LoanConfiguration, Long>{

    LoanConfiguration  getLoanConfigurationById(LoanConfigBankCodeRequest loanConfigBankCodeRequest);

    @Query("SELECT lc FROM LoanConfiguration lc WHERE lc.bank.bankCode = :bankCode")
    Optional<LoanConfiguration> findByBankCode(String bankCode);
}
