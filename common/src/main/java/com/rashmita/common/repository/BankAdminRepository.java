package com.rashmita.common.repository;
import com.rashmita.common.entity.Bank;
import com.rashmita.common.entity.BankAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAdminRepository extends JpaRepository<BankAdmin, Long> {

    Optional<BankAdmin> findByBank(Bank bank);

    @Query("select ba from BankAdmin ba where ba.email = :email")
    Optional<BankAdmin> findByEmail(@Param("email") String email);

}
