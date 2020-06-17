package com.mmadu.service.controllers;

import com.mmadu.service.exceptions.DomainNotFoundException;
import com.mmadu.service.models.GroupUserRemovalRequest;
import com.mmadu.service.models.NewGroupRequest;
import com.mmadu.service.models.NewGroupUserRequest;
import com.mmadu.service.models.UserView;
import com.mmadu.service.repositories.AppDomainRepository;
import com.mmadu.service.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/domains/{domainId}/groups")
public class GroupManagementController {
    @Autowired
    private AppDomainRepository appDomainRepository;
    @Autowired
    private GroupService groupService;

    @InitBinder
    void validateDomainId(@PathVariable("domainId") String domainId) {
        if (!appDomainRepository.existsById(domainId)) {
            throw new DomainNotFoundException();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('group.create')")
    public void addGroup(@PathVariable("domainId") String domainId,
                         @RequestBody @Valid NewGroupRequest request) {
        groupService.addGroup(domainId, request);
    }

    @PostMapping("/{groupIdentifier}/users/{userExternalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('group.add_user')")
    public void addUserToGroup(@PathVariable("domainId") String domainId,
                               @PathVariable("groupIdentifier") String groupIdentifier,
                               @PathVariable("userExternalId") String userExternalId) {
        NewGroupUserRequest request = new NewGroupUserRequest();
        request.setId(userExternalId);
        request.setGroup(groupIdentifier);
        groupService.addUserToGroup(domainId, request);
    }

    @DeleteMapping("/{groupIdentifier}/users/{userExternalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('group.remove_user')")
    public void removeUserFromGroup(@PathVariable("domainId") String domainId,
                                    @PathVariable("groupIdentifier") String groupIdentifier,
                                    @PathVariable("userExternalId") String userExternalId) {
        GroupUserRemovalRequest request = new GroupUserRemovalRequest();
        request.setId(userExternalId);
        request.setGroup(groupIdentifier);
        groupService.removeUserFromGroup(domainId, request);
    }

    @GetMapping("/{groupIdentifier}/users")
    @PreAuthorize("hasAuthority('group.read')")
    public Page<UserView> getUsersInGroup(
            @PathVariable("domainId") String domainId,
            @PathVariable("groupIdentifier") String group, Pageable p) {
        return groupService.getUsersInGroup(domainId, group, p);
    }

}
