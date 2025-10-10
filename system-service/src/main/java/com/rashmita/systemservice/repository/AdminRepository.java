package com.rashmita.systemservice.repository;

import com.rashmita.systemservice.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,String> {
    @Query("SELECT a.username FROM Admin a")
    List<String> findAllUsernames();
    Optional<Admin> findByUsername(String email);
    @Query("select a from Admin a where a.email = :email")
    Optional<Admin> findByEmail(@Param("email") String email);

    String email(String email);
}
