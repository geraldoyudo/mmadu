package com.mmadu.service.controllers;

import com.mmadu.service.models.ResetUserPasswordRequest;
import com.mmadu.service.models.UpdateRequest;
import com.mmadu.service.models.UserUpdateRequest;
import com.mmadu.service.models.UserView;
import com.mmadu.service.services.AuthorityManagementService;
import com.mmadu.service.services.GroupService;
import com.mmadu.service.services.RoleManagementService;
import com.mmadu.service.services.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/domains/{domainId}/users")
public class UserManagementController {
    @Autowired
    private UserManagementService userManagementService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private RoleManagementService roleManagementService;
    @Autowired
    private AuthorityManagementService authorityManagementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('user.create')")
    public void createUserInDomain(@RequestBody UserView user,
                                   @PathVariable("domainId") String domainId) {
        userManagementService.createUser(domainId, user);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user.read')")
    public Page<UserView> getAllUsersInDomain(@PathVariable("domainId") String domainId, Pageable p) {
        return userManagementService.getAllUsers(domainId, p);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('user.read')")
    public UserView getUserByDomainAndExternalId(@PathVariable("domainId") String domainId,
                                                 @PathVariable("userId") String externalId) {
        return userManagementService.getUserByDomainIdAndExternalId(domainId, externalId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user.delete')")
    public void deleteUserByDomainAndExternalId(@PathVariable("domainId") String domainId,
                                                @PathVariable("userId") String externalId) {
        userManagementService.deleteUserByDomainAndExternalId(domainId, externalId);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user.update')")
    public void updateUserByDomainAndExternalId(@PathVariable("domainId") String domainId,
                                                @PathVariable("userId") String externalId,
                                                @RequestBody UserView userView) {
        userManagementService.updateUser(domainId, externalId, userView);
    }

    @GetMapping("/load")
    @PreAuthorize("hasAuthority('user.load')")
    public UserView loadUserByUsername(@PathVariable("domainId") String domainId,
                                       @RequestParam("username") String username) {
        UserView userView = userManagementService.getUserByDomainIdAndUsername(domainId, username);
        userView.setGroups(List.copyOf(groupService.getGroups(domainId, userView.getId())));
        userView.setRoles(
                List.copyOf(
                        roleManagementService.getUserRoles(domainId, userView.getId())
                )
        );
        Set<String> authorities = new HashSet<>();
        authorities.addAll(authorityManagementService.getUserAuthorities(domainId, userView.getId()));
        authorities.addAll(roleManagementService.getAuthoritiesForRoles(domainId, userView.getRoles()));
        userView.setAuthorities(List.copyOf(authorities));
        return userView;
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('user.read')")
    public Page<UserView> queryUsers(@PathVariable("domainId") String domainId,
                                     @RequestParam("query") String query,
                                     Pageable p) {
        return userManagementService.queryUsers(domainId, query, p);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user.update')")
    public void patchUpdateUsers(@RequestBody UserUpdateRequest request,
                                 @PathVariable("domainId") String domainId) {
        UpdateRequest updateRequest = new UpdateRequest(request.getUpdates());
        userManagementService.patchUpdateUsers(domainId, request.getQuery(), updateRequest);
    }

    @PostMapping("/{userId}/resetPassword")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user.reset_password')")
    public void resetUserPassword(@RequestBody @Valid ResetUserPasswordRequest request,
                                  @PathVariable("domainId") String domainId,
                                  @PathVariable("userId") String userId) {
        userManagementService.resetUserPassword(domainId, userId, request.getNewPassword());
    }
}
