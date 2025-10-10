package com.rashmita.systemservice.service;

import com.rashmita.systemservice.model.*;

import java.security.Principal;

public interface AccessGroupService {
        ServerResponse<?> createAccessGroup(CreateAccessGroupModel createAccessGroupModel, Principal connectedUser);
        ServerResponse<?> updateAccessGroup(UpdateAccessGroupRequest request);
        ServerResponse<?> deleteAccessGroup(DeleteAccessGroupRequest deleteAccessGroupRequest);
        ServerResponse<?> getAllAccessGroup(SearchParam searchParam);
        ServerResponse<?> getAccessGroupDetail(FetchAccessGroupDetail fetchAccessGroupDetail);
        ServerResponse<?> blockAccessGroup(BlockAccessGroupRequest blockAccessGroupRequest, Principal connectedUser);
        ServerResponse<?> unblockAccessGroup(UnblockAccessGroupRequest unblockAccessGroupRequest, Principal connectedUser);

}
