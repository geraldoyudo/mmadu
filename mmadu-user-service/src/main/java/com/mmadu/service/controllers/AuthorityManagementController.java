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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/domains/{domainId}/roles")
public class AuthorityManagementController {
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

    @PostMapping
    public void saveRoles(@PathVariable("domainId") String domainId,
                          @RequestBody @Valid @Size(min = 1, message = "roles required") List<SaveRoleRequest> roles) {
        roleManagementService.saveRoles(domainId, roles);
    }

    @GetMapping
    public Page<RoleData> getRoles(@PathVariable("domainId") String domainId, Pageable p) {
        return roleManagementService.getRoles(domainId, p);
    }

    @DeleteMapping("/{roleIdentifier}")
    public void deleteRole(@PathVariable("domainId") String domainId,
                           @PathVariable("roleIdentifier") String identifier) {
        roleManagementService.deleteRole(domainId, identifier);
    }

    @PostMapping("/users/{userId}/addRoles")
    public void grantUserRoles(@PathVariable("domainId") String domainId,
                               @PathVariable("userId") String userId,
                               @RequestBody @Valid @Size(min = 1, message = "role identifiers required")
                                       List<String> roleIdentifiers) {
        roleManagementService.grantUserRoles(domainId, userId, roleIdentifiers);
    }

    @PostMapping("/users/{userId}/removeRoles")
    public void revokeUserRoles(
            @PathVariable("domainId") String domainId,
            @PathVariable("userId") String userId,
            @RequestBody @Valid @Size(min = 1, message = "role identifiers required")
                    List<String> roleIdentifiers
    ) {
        roleManagementService.revokeUserRoles(domainId, userId, roleIdentifiers);
    }

    @PostMapping("/authorities/add")
    public void addAuthorityToRole(@PathVariable("domainId") String domainId,
                                   @RequestBody @Valid @Size(min = 1, message = "roleAuthorities required")
                                           List<RoleAuthorityUpdateRequest> roleAuthorities) {
        roleManagementService.addAuthorityToRole(domainId, roleAuthorities);
    }

    @PostMapping("/authorities/remove")
    public void removeAuthorityFromRole(@PathVariable("domainId") String domainId,
                                        @RequestBody @Valid @Size(min = 1, message = "roleAuthorities required")
                                                List<RoleAuthorityUpdateRequest> roleAuthorities) {
        roleManagementService.removeAuthorityFromRole(domainId, roleAuthorities);
    }
}
