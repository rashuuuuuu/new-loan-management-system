package com.rashmita.systemservice.controller;
import com.rashmita.systemservice.constants.ApiConstants;
import com.rashmita.systemservice.model.*;
import com.rashmita.systemservice.service.AccessGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
@RestController
@RequestMapping(ApiConstants.ACCESS_GROUP)
@RequiredArgsConstructor
public class AccessGroupController {
    private final AccessGroupService accessGroupService;

    @PostMapping(ApiConstants.CREATE)
    public ServerResponse addAccessGroup(@RequestBody @Valid CreateAccessGroupModel createAccessGroupModel,
                                         Principal connectedUser) {
        return accessGroupService.createAccessGroup(createAccessGroupModel, connectedUser);
    }

    @PostMapping()
    public ServerResponse<?> getAllAccessGroups(@RequestBody @Valid SearchParam searchParam) {
        return accessGroupService.getAllAccessGroup(searchParam);
    }

    @PostMapping(ApiConstants.GET + ApiConstants.SLASH + ApiConstants.DETAIL)
    public ServerResponse<?> getAccessGroupDetail(@RequestBody @Valid FetchAccessGroupDetail fetchAccessGroupDetail) {
        return accessGroupService.getAccessGroupDetail(fetchAccessGroupDetail);
    }

    @PostMapping(ApiConstants.UPDATE)
    public ServerResponse<?> updateAccessGroup(@RequestBody @Valid UpdateAccessGroupRequest request){
        return accessGroupService.updateAccessGroup(request);

    }

    @PostMapping(ApiConstants.DELETE)

    public ServerResponse<?> deleteAccessGroup(@RequestBody @Valid DeleteAccessGroupRequest request){
        return accessGroupService.deleteAccessGroup(request);
    }

    @PostMapping(ApiConstants.BLOCK)
    public ServerResponse<?> blockAccessGroup(@RequestBody @Valid BlockAccessGroupRequest blockAccessGroupRequest, Principal connectedUser){
        return accessGroupService.blockAccessGroup(blockAccessGroupRequest, connectedUser);
    }

    @PostMapping(ApiConstants.UNBLOCK)
    public ServerResponse<?> unblockAccessGroup(@RequestBody @Valid UnblockAccessGroupRequest unblockAccessGroupRequest, Principal connectedUser){
        return accessGroupService.unblockAccessGroup(unblockAccessGroupRequest, connectedUser);
    }
}
