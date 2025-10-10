package com.rashmita.systemservice.mapper;
import com.rashmita.systemservice.entity.AccessGroup;

import com.rashmita.systemservice.model.*;
import com.rashmita.systemservice.repository.AccessGroupRepository;
import com.rashmita.systemservice.repository.AccessGroupTypeRepository;
import com.rashmita.systemservice.repository.StatusRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccessGroupMapper {
    private final ModelMapper modelMapper;
    private final StatusRepository statusRepository;
    private final AccessGroupRepository accessGroupRepository;
    private final AccessGroupTypeRepository accessGroupTypeRepository;

    @Autowired
    public AccessGroupMapper(ModelMapper modelMapper,
                             StatusRepository statusRepository,
                             AccessGroupRepository accessGroupRepository,
                             AccessGroupTypeRepository accessGroupTypeRepository) {
        this.modelMapper = modelMapper;
        this.statusRepository = statusRepository;
        this.accessGroupRepository = accessGroupRepository;
        this.accessGroupTypeRepository = accessGroupTypeRepository;
    }
    public AccessGroup toEntity(CreateAccessGroupModel request) {
        AccessGroup accessGroup = modelMapper.map(request, AccessGroup.class);
        accessGroup.setCreatedAt(new Date());
        accessGroup.setSuperAdminGroup(false);
        accessGroup.setStatus(statusRepository.getStatusByName("CREATED"));
        accessGroup.setAccessGroupType(accessGroupTypeRepository.findAccessGroupTypeByName("BANK"));
        return accessGroupRepository.save(accessGroup);
    }

    public SearchAccessGroupResponse entityToResponse(AccessGroup accessGroup) {
        if (accessGroup == null) return null;

        SearchAccessGroupResponse response = modelMapper.map(accessGroup, SearchAccessGroupResponse.class);

        // Map nested Status manually if needed
        if (accessGroup.getStatus() != null) {
            response.setStatus(modelMapper.map(accessGroup.getStatus(), StatusDto.class));
        }
        return response;
    }

    public List<SearchAccessGroupResponse> getAccessGroupResponses(List<AccessGroup> accessGroups) {
        return accessGroups.stream()
                .map(this::entityToResponse)
                .collect(Collectors.toList());
    }

    public AccessGroupDetailDto getAccessGroupDetailDto(AccessGroup accessGroup) {
        if (accessGroup == null) return null;
        return modelMapper.map(accessGroup, AccessGroupDetailDto.class);
    }

    public AccessGroup updateAccessGroup(UpdateAccessGroupRequest request, AccessGroup accessGroup) {
        if (request == null || accessGroup == null) return null;

        modelMapper.map(request, accessGroup);
        accessGroup.setUpdatedAt(new Date());
        return accessGroup;
    }
}
