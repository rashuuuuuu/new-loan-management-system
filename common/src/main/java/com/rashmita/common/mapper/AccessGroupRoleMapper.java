package com.rashmita.common.mapper;
import com.rashmita.common.entity.AccessGroup;
import com.rashmita.common.entity.AccessGroupTypeRoleMap;
import com.rashmita.common.entity.Roles;
import com.rashmita.common.model.AssignRoleModel;
import com.rashmita.common.repository.AccessGroupTypeRoleMapRepository;
import com.rashmita.common.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessGroupRoleMapper {
    @Autowired
    AccessGroupTypeRoleMapRepository accessGroupTypeRoleMapRepository;
    @Autowired
    protected RoleRepository roleRepository;

    public List<AccessGroupTypeRoleMap> createAccessGroupRoleMap(AccessGroup accessGroup, List<AssignRoleModel> roles) {
        List<Long> assignedRoleId = roles.stream().map(AssignRoleModel::getRoleId).toList();
        List<Roles> allRoles = roleRepository.getAllRoles();
        List<AccessGroupTypeRoleMap> accessGroupRoleMaps = allRoles.stream().map(role -> {
            AccessGroupTypeRoleMap accessGroupRoleMap = new AccessGroupTypeRoleMap();
            accessGroupRoleMap.setAccessGroup(accessGroup);
            accessGroupRoleMap.setAccessGroupType(accessGroup.getAccessGroupType());
            accessGroupRoleMap.setIsActive(assignedRoleId.contains(role.getId()));
            accessGroupRoleMap.setRole(role);
            return accessGroupRoleMap;
        }).collect(Collectors.toList());

        return accessGroupTypeRoleMapRepository.saveAll(accessGroupRoleMaps);
    }
    public List<AccessGroupTypeRoleMap> updateAccessGroupRoleMap(AccessGroup accessGroup, List<AssignRoleModel> roles) {
        List<Long> assignedRoleId = roles.stream().map(AssignRoleModel::getRoleId).toList();
        List<AccessGroupTypeRoleMap> existingRoleMaps= accessGroupTypeRoleMapRepository.findByAccessGroup(accessGroup);

        Map<Long, AccessGroupTypeRoleMap> existingRoleMapById = existingRoleMaps.stream()
                .collect(Collectors.toMap(roleMap -> roleMap.getRole().getId(), roleMap -> roleMap));

        List<AccessGroupTypeRoleMap> updatedRoleMaps = new ArrayList<>();
        for (Long roleId : assignedRoleId) {
            AccessGroupTypeRoleMap roleMap = existingRoleMapById.get(roleId);
            if (roleMap != null) {
                roleMap.setIsActive(true);
                updatedRoleMaps.add(roleMap);
            } else {
                Roles role = roleRepository.findById(roleId).orElseThrow();
                AccessGroupTypeRoleMap newRoleMap = new AccessGroupTypeRoleMap();
                newRoleMap.setAccessGroup(accessGroup);
                newRoleMap.setRole(role);
                newRoleMap.setIsActive(true);
                updatedRoleMaps.add(newRoleMap);
            }
        }
        existingRoleMaps.stream()
                .filter(roleMap -> !updatedRoleMaps.contains(roleMap))
                .forEach(updatedRoleMaps::add);

        return accessGroupTypeRoleMapRepository.saveAll(updatedRoleMaps);

    }



}
