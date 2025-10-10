package com.rashmita.systemservice.repository;

import com.rashmita.systemservice.entity.AccessGroup;
import com.rashmita.systemservice.entity.AccessGroupType;
import com.rashmita.systemservice.entity.AccessGroupTypeRoleMap;
import com.rashmita.systemservice.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessGroupTypeRoleMapRepository extends JpaRepository<AccessGroupTypeRoleMap,String> {
    boolean existsByAccessGroupTypeAndAccessGroupAndRole(AccessGroupType accessGroupType, AccessGroup accessGroup, Roles role);

    List<AccessGroupTypeRoleMap> findByAccessGroup(AccessGroup accessGroup);
}
