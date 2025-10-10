package com.rashmita.common.repository;


import com.rashmita.common.entity.AccessGroupType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AccessGroupTypeRepository extends JpaRepository<AccessGroupType, String> {
    @Query("SELECT agt.name FROM AccessGroupType agt WHERE agt.name IN :names")
    List<String> getAllByNames(@Param("names") List<String> names);
    Optional<AccessGroupType> findByName(String name);
    @Query("SELECT agt FROM AccessGroupType agt WHERE agt.name = :name")
    AccessGroupType findAccessGroupTypeByName(@Param("name") String name);


    boolean existsByName(String name);
}
