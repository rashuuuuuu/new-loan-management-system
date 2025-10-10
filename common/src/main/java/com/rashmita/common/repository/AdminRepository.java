package com.rashmita.common.repository;
import com.rashmita.common.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,String> {
    @Query("SELECT a.username FROM Admin a")
    List<String> findAllUsernames();

    Optional<Admin> findByEmail(String email);



}
