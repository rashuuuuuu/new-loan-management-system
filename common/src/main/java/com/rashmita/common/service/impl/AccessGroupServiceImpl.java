package com.rashmita.common.service.impl;

import com.rashmita.common.constants.StatusConstants;
import com.rashmita.common.entity.AccessGroup;
import com.rashmita.common.mapper.AccessGroupMapper;
import com.rashmita.common.mapper.AccessGroupRoleMapper;
import com.rashmita.common.model.*;
import com.rashmita.common.repository.AccessGroupRepository;
import com.rashmita.common.repository.AccessGroupSearchRepository;
import com.rashmita.common.repository.StatusRepository;
import com.rashmita.common.service.AccessGroupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccessGroupServiceImpl implements AccessGroupService {
    private final AccessGroupRepository accessGroupRepository;
    private final AccessGroupMapper accessGroupMapper;
    private final AccessGroupRoleMapper accessGroupRoleMapMapper;
    private final SearchResponse searchResponse;
    private final AccessGroupSearchRepository accessGroupSearchRepository;
    private final StatusRepository statusRepository;

    @Override
    @Transactional
    public ServerResponse createAccessGroup(CreateAccessGroupModel createAccessGroupModel, Principal connectedUser) {
        Optional<AccessGroup> existedAccessGroup = accessGroupRepository.findByName(createAccessGroupModel.getName());
        if (existedAccessGroup.isPresent()) {
            return ResponseUtility.getFailedServerResponse("This name is already taken. Please use different name.");
        }
        AccessGroup accessGroup = accessGroupMapper.toEntity(createAccessGroupModel);
        accessGroupRoleMapMapper.createAccessGroupRoleMap(accessGroup, createAccessGroupModel.getRoles());
        return ResponseUtility.getSuccessfulServerResponse("Access group created");
    }
    @Override
    @Transactional
    public ServerResponse updateAccessGroup(UpdateAccessGroupRequest request) {
        Optional<AccessGroup> existedAccessGroup = accessGroupRepository.findByName(request.getName());
        if (existedAccessGroup.isPresent() && !existedAccessGroup.get().getName().equals(request.getName())) {
            return ResponseUtility.getFailedServerResponse("Access group name already exists");
        }
        if (existedAccessGroup.isPresent()) {
            AccessGroup updatedAccessGroup = accessGroupMapper.updateAccessGroup(request, existedAccessGroup.get());
            accessGroupRepository.save(updatedAccessGroup);
            accessGroupRoleMapMapper.updateAccessGroupRoleMap(updatedAccessGroup, request.getRoles());
            return ResponseUtility.getSuccessfulServerResponse("Access group updated successfully");
        } else {
            return ResponseUtility.getFailedServerResponse("Access group not found");
        }
    }

    @Override
    public ServerResponse<?> deleteAccessGroup(DeleteAccessGroupRequest deleteAccessGroupRequest) {
        Optional<AccessGroup> accessGroup = accessGroupRepository.findByName(deleteAccessGroupRequest.getName());
        if (accessGroup.isEmpty()) {
            return ResponseUtility.getFailedServerResponse("Access group not found");
        } else {
            AccessGroup accessGroup1 = accessGroup.get();
            if ("DELETED".equals(accessGroup1.getStatus().getName())) {
                return ResponseUtility.getFailedServerResponse("Access group not found");
            }
            accessGroup1.setStatus(statusRepository.getStatusByName(StatusConstants.DELETED.getName()));
            accessGroupRepository.save(accessGroup1);
            return ResponseUtility.getSuccessfulServerResponse("Access group deleted successfully");
        }
    }

    @Override
    public ServerResponse<?> getAllAccessGroup(SearchParam searchParam) {
        SearchResponseWithMapperBuilder<AccessGroup, SearchAccessGroupResponse> responseBuilder = SearchResponseWithMapperBuilder
                .<AccessGroup, SearchAccessGroupResponse>builder()
                .count(accessGroupSearchRepository::count)
                .searchData(accessGroupSearchRepository::getAll)
                .mapperFunction(this.accessGroupMapper::getAccessGroupResponses)
                .searchParam(searchParam)
                .build();
        PageableResponse<SearchAccessGroupResponse> response = searchResponse.getSearchResponse(responseBuilder);
        return ResponseUtility.getSuccessfulServerResponse(response, "Access groups fetched successfully.");
    }

//    @Override
//    public ServerResponse<?> getActiveAccessGroup() {
//        Status status = statusRepository.findByName(StatusConstants.ACTIVE.getName());
//        List<AccessGroup> accessGroups = accessGroupRepository.findAllByStatus(status);
//        if (accessGroups.isEmpty()) {
//            return ResponseUtility.getFailedServerResponse("No active access group found");
//        }
//        else {
//            List<AccessGroupDetailDto> accessGroupDetailDto = accessGroupMapper.mapToAccessGroupDtoLists(accessGroups);
//            return ResponseUtility.getSuccessfulServerResponse(accessGroupDetailDto, "Access groups fetched successfully");
//        }
//    }

    @Override
    public ServerResponse<?> getAccessGroupDetail(FetchAccessGroupDetail fetchAccessGroupDetail) {
        Optional<AccessGroup> accessGroup = accessGroupRepository.findByName(fetchAccessGroupDetail.getName());
        if (accessGroup.isEmpty()) {
            return ResponseUtility.getFailedServerResponse("Access group  not found");
        } else {
            AccessGroupDetailDto accessGroupDetailDto = accessGroupMapper.getAccessGroupDetailDto(accessGroup.get());
            return ResponseUtility.getSuccessfulServerResponse(accessGroupDetailDto, "Access group fetched successfully");
        }
    }

    @Override
    public ServerResponse<?> blockAccessGroup(BlockAccessGroupRequest blockAccessGroupRequest, Principal connectedUSer) {
        Optional<AccessGroup> optionalAccessGroup = accessGroupRepository.findByName(blockAccessGroupRequest.getName());
        if (optionalAccessGroup.isEmpty() || StatusConstants.DELETED.getName().equals(optionalAccessGroup.get().getStatus().getName())) {
            return ResponseUtility.getFailedServerResponse("AccessGroup not found");
        } else if (StatusConstants.BLOCKED.getName().equals(optionalAccessGroup.get().getStatus().getName())) {
            return ResponseUtility.getSuccessfulServerResponse("AccessGroup is already blocked");
        }
        AccessGroup accessGroup = optionalAccessGroup.get();
        accessGroup.setStatus(statusRepository.getStatusByName(StatusConstants.BLOCKED.getName()));
        accessGroupRepository.save(accessGroup);
        return ResponseUtility.getSuccessfulServerResponse("AccessGroup blocked successfully");
    }

    @Override
    public ServerResponse<?> unblockAccessGroup(UnblockAccessGroupRequest unblockAccessGroupRequest, Principal connectedUser) {
        Optional<AccessGroup> optionalAccessGroup = accessGroupRepository.findByName(unblockAccessGroupRequest.getName());
        if (optionalAccessGroup.isEmpty() || StatusConstants.DELETED.getName().equals(optionalAccessGroup.get().getStatus().getName())){
            return ResponseUtility.getFailedServerResponse("AccessGroup not found");
        }
        else if(StatusConstants.BLOCKED.getName().equals(optionalAccessGroup.get().getStatus().getName())){
            AccessGroup accessGroup = optionalAccessGroup.get();
            accessGroup.setStatus(statusRepository.getStatusByName(StatusConstants.ACTIVE.getName()));
            accessGroupRepository.save(accessGroup);
            return ResponseUtility.getSuccessfulServerResponse("AccessGroup unblocked successfully");
        }
        return ResponseUtility.getFailedServerResponse("AccessGroup cannot be unblock");
    }
}


