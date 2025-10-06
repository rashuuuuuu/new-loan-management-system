package com.rashmita.systemservice.repository;
import com.rashmita.systemservice.entity.Roles;
import com.rashmita.systemservice.constants.RoleEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Roles, Integer> {
    Optional<Roles> findByName(RoleEnum name);
}
