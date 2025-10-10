package com.rashmita.common.repository;

import com.rashmita.common.entity.AccessGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccessGroupRepository extends JpaRepository<AccessGroup, Long> {

//    AccessGroup findByName(String accessGroupName);

    @Query("SELECT ag.name FROM AccessGroup ag WHERE ag.name in :names")
    List<String> getAllGroupsByName(@Param("names") List<String> list);

    Optional<AccessGroup> findByName(String name);

    AccessGroup getByName(String name);

    boolean existsByName(String name);
}
