package com.rashmita.systemservice.repository;

import com.rashmita.systemservice.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    Optional<Status> findByName(String name);

    @Query("SELECT s FROM Status s WHERE s.name IN :names")
    List<Status> getAllStatusByNames(@Param("names") List<String> names);

    Optional<Status> getByName(String name);
    @Query("SELECT s FROM Status s WHERE s.name = :name")
    Status getStatusByName(@Param("name") String name);
    Optional<Status> findByNameIgnoreCase(String name);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM Status s WHERE LOWER(s.name) = LOWER(:name)")
    boolean existsByNameIgnoreCase(@Param("name") String name);
}