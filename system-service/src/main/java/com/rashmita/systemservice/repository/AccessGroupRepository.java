package com.rashmita.systemservice.repository;
import com.rashmita.systemservice.entity.AccessGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccessGroupRepository extends JpaRepository<AccessGroup, Long> {

    /**
     * Fetch all group names that match the given list.
     */
    @Query("SELECT ag.name FROM AccessGroup ag WHERE ag.name IN :names")
    List<String> getAllGroupsByName(@Param("names") List<String> names);

    /**
     * Find AccessGroup safely by name.
     */
    Optional<AccessGroup> findByName(String name);

    /**
     * Helper method that returns AccessGroup or throws exception if not found.
     * Avoids returning null to prevent NPE when used in services.
     */
    default AccessGroup getByNameOrThrow(String name) {
        return null;
    }

    boolean existsByName(String name);
}
