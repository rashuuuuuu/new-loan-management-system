package com.rashmita.common.DataInitialization;

import com.rashmita.common.config.*;
import com.rashmita.common.constants.StatusConstants;
import com.rashmita.common.entity.*;
import com.rashmita.common.model.*;
import com.rashmita.common.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final AccessGroupRepository accessGroupRepository;
    private final AccessGroupTypeRepository accessGroupTypeRepository;
    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;
    private final AccessGroupTypeRoleMapRepository accessGroupTypeRoleMapRepository;

    private final AccessGroupsConfiguration accessGroupInitConfiguration;
    private final AccessGroupTypeConfiguration accessGroupTypeConfiguration;
    private final RoleConfiguration rolesInitConfiguration;
    private final StatusConfiguration statusInitConfiguration;
    private final AccessGroupTypeRoleMapConfiguration accessGroupTypeRoleMapConfiguration;
    @Transactional
    @PostConstruct
    public void initializeData() {
        populateStatus();
        populateRoles();
        populateAccessGroupTypes();
        populateAccessGroup();
        populateAccessGroupTypeRoleMap();
    }

    private void populateStatus() {
        List<StatusDto> statuses = statusInitConfiguration.getStatusList();
        for (StatusDto dto : statuses) {
            if (!statusRepository.existsByNameIgnoreCase(dto.getName())) {
                Status status = new Status();
                status.setName(dto.getName().toUpperCase());
                status.setDescription(dto.getDescription());
                status.setIcon(dto.getIcon());
                statusRepository.save(status);
            }
        }
        log.info(" Status populated without duplicates.");
    }

    private void populateRoles() {
        List<RoleDto> roles = rolesInitConfiguration.getRolesList();
        for (RoleDto dto : roles) {
            if (!roleRepository.existsByName(dto.getName())) {
                Roles role = new Roles();
                role.setName(dto.getName());
                role.setDescription(dto.getDescription());
                role.setPermission(dto.getPermission());
                roleRepository.save(role);
            }
        }
        log.info(" Roles populated without duplicates.");
    }

    private void populateAccessGroupTypes() {
        List<AccessGroupTypeDto> types = accessGroupTypeConfiguration.getGroupTypeYmlList();
        for (AccessGroupTypeDto dto : types) {
            if (!accessGroupTypeRepository.existsByName((dto.getName()))){
                AccessGroupType type = new AccessGroupType();
                type.setName(dto.getName());
                accessGroupTypeRepository.save(type);
            }
        }
        log.info(" AccessGroupType populated without duplicates.");
    }

    private void populateAccessGroup() {
        List<AccessGroupDto> groups = accessGroupInitConfiguration.getAccessGroupsList();

        for (AccessGroupDto dto : groups) {
            if (!accessGroupRepository.existsByName(dto.getName())) {
                AccessGroup group = new AccessGroup();
                group.setName(dto.getName());
                group.setDescription(dto.getDescription());

                Status status = statusRepository.findByName(StatusConstants.CREATED.getName())
                        .orElseThrow(() -> new RuntimeException("Status 'CREATED' not found"));
                group.setStatus(status);

                AccessGroupType type = accessGroupTypeRepository.findByName(dto.getAccessGroupType().getName())
                        .orElseThrow(() -> new RuntimeException(
                                "AccessGroupType not found: " + dto.getAccessGroupType().getName()
                        ));
                group.setAccessGroupType(type);

                accessGroupRepository.save(group);
            }
        }
        log.info(" AccessGroup populated without duplicates.");
    }

    private void populateAccessGroupTypeRoleMap() {
        List<AccessGroupTypeRoleMapDto> mapDtos = accessGroupTypeRoleMapConfiguration.getAccessGroupTypeRoleMapList();

        for (AccessGroupTypeRoleMapDto dto : mapDtos) {
            AccessGroupType type = accessGroupTypeRepository.findByName(dto.getAccessGroupTypeName()).orElse(null);
            if (type == null) continue;

            AccessGroup group = accessGroupRepository.findByName(dto.getAccessGroupName()).orElse(null);
            if (group == null) continue;

            Roles role = roleRepository.findByName(dto.getRoleName());
            if (role == null) continue;

            boolean exists = accessGroupTypeRoleMapRepository.existsByAccessGroupTypeAndAccessGroupAndRole(type, group, role);
            if (!exists) {
                AccessGroupTypeRoleMap map = new AccessGroupTypeRoleMap();
                map.setAccessGroupType(type);
                map.setAccessGroup(group);
                map.setRole(role);
                map.setIsActive(true);
                accessGroupTypeRoleMapRepository.save(map);
            }
        }
        log.info(" AccessGroupTypeRoleMap populated without duplicates.");
    }
}
