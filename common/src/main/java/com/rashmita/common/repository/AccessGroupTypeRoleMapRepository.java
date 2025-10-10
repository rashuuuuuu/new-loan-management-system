package com.rashmita.common.repository;

import com.rashmita.common.entity.AccessGroup;
import com.rashmita.common.entity.AccessGroupType;
import com.rashmita.common.entity.AccessGroupTypeRoleMap;
import com.rashmita.common.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessGroupTypeRoleMapRepository extends JpaRepository<AccessGroupTypeRoleMap,String> {
    boolean existsByAccessGroupTypeAndAccessGroupAndRole(AccessGroupType accessGroupType, AccessGroup accessGroup, Roles role);

    List<AccessGroupTypeRoleMap> findByAccessGroup(AccessGroup accessGroup);
}
