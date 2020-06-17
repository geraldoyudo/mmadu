package com.mmadu.service.controllers;

import com.mmadu.service.exceptions.DomainNotFoundException;
import com.mmadu.service.models.RoleAuthorityUpdateRequest;
import com.mmadu.service.models.RoleData;
import com.mmadu.service.models.SaveRoleRequest;
import com.mmadu.service.repositories.AppDomainRepository;
import com.mmadu.service.services.RoleManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/domains/{domainId}/roles")
public class RoleManagementController {
    private AppDomainRepository appDomainRepository;
    private RoleManagementService roleManagementService;

    @Autowired
    public void setAppDomainRepository(AppDomainRepository appDomainRepository) {
        this.appDomainRepository = appDomainRepository;
    }

    @Autowired
    public void setRoleManagementService(RoleManagementService roleManagementService) {
        this.roleManagementService = roleManagementService;
    }

    @InitBinder
    void validateDomainId(@PathVariable("domainId") String domainId) {
        if (!appDomainRepository.existsById(domainId)) {
            throw new DomainNotFoundException();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('role.update')")
    public void saveRoles(@PathVariable("domainId") String domainId,
                          @RequestBody @Valid @Size(min = 1, message = "roles required") List<SaveRoleRequest> roles) {
        roleManagementService.saveRoles(domainId, roles);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('role.read')")
    public Page<RoleData> getRoles(@PathVariable("domainId") String domainId, Pageable p) {
        return roleManagementService.getRoles(domainId, p);
    }

    @DeleteMapping("/{roleIdentifier}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('role.delete')")
    public void deleteRole(@PathVariable("domainId") String domainId,
                           @PathVariable("roleIdentifier") String identifier) {
        roleManagementService.deleteRole(domainId, identifier);
    }

    @PostMapping(path = "/users/{userId}/addRoles", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('role.grant_user')")
    public void grantUserRoles(@PathVariable("domainId") String domainId,
                               @PathVariable("userId") String userId,
                               @RequestBody @Valid @Size(min = 1, message = "role identifiers required")
                                       List<String> roleIdentifiers) {
        roleManagementService.grantUserRoles(domainId, userId, roleIdentifiers);
    }

    @PostMapping(path = "/users/{userId}/removeRoles", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('role.revoke_user')")
    public void revokeUserRoles(
            @PathVariable("domainId") String domainId,
            @PathVariable("userId") String userId,
            @RequestBody @Valid @Size(min = 1, message = "role identifiers required")
                    List<String> roleIdentifiers
    ) {
        roleManagementService.revokeUserRoles(domainId, userId, roleIdentifiers);
    }

    @PostMapping(path = "/authorities/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('role.add_authority')")
    public void addAuthorityToRole(@PathVariable("domainId") String domainId,
                                   @RequestBody @Valid @Size(min = 1, message = "roleAuthorities required")
                                           List<RoleAuthorityUpdateRequest> roleAuthorities) {
        roleManagementService.addAuthorityToRole(domainId, roleAuthorities);
    }

    @PostMapping(path = "/authorities/remove", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('role.remove_authority')")
    public void removeAuthorityFromRole(@PathVariable("domainId") String domainId,
                                        @RequestBody @Valid @Size(min = 1, message = "roleAuthorities required")
                                                List<RoleAuthorityUpdateRequest> roleAuthorities) {
        roleManagementService.removeAuthorityFromRole(domainId, roleAuthorities);
    }
}
