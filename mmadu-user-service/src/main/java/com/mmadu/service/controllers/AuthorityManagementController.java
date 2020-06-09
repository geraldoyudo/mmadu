package com.mmadu.service.controllers;

import com.mmadu.service.exceptions.DomainNotFoundException;
import com.mmadu.service.models.AuthorityData;
import com.mmadu.service.repositories.AppDomainRepository;
import com.mmadu.service.services.AuthorityManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/domains/{domainId}/authorities")
public class AuthorityManagementController {
    private AppDomainRepository appDomainRepository;
    private AuthorityManagementService authorityManagementService;

    @Autowired
    public void setAppDomainRepository(AppDomainRepository appDomainRepository) {
        this.appDomainRepository = appDomainRepository;
    }

    @Autowired
    public void setAuthorityManagementService(AuthorityManagementService authorityManagementService) {
        this.authorityManagementService = authorityManagementService;
    }

    @InitBinder
    void validateDomainId(@PathVariable("domainId") String domainId) {
        if (!appDomainRepository.existsById(domainId)) {
            throw new DomainNotFoundException();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAuthorities(@PathVariable("domainId") String domainId,
                                @RequestBody @Valid @Size(min = 1, message = "authorities required") List<AuthorityData> authorities) {
        authorityManagementService.saveAuthorities(domainId, authorities);
    }

    @GetMapping
    public Page<AuthorityData> getAuthorities(@PathVariable("domainId") String domainId, Pageable p) {
        return authorityManagementService.getAuthorities(domainId, p);
    }

    @DeleteMapping("/{authorityIdentifier}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthority(@PathVariable("domainId") String domainId,
                                @PathVariable("authorityIdentifier") String identifier) {
        authorityManagementService.deleteAuthority(domainId, identifier);
    }

    @PostMapping(path = "/users/{userId}/addAuthorities", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void grantUserAuthorities(@PathVariable("domainId") String domainId,
                                     @PathVariable("userId") String userId,
                                     @RequestBody @Valid @Size(min = 1, message = "authority identifiers required")
                                             List<String> authorityIdentifiers) {
        authorityManagementService.grantUserAuthorities(domainId, userId, authorityIdentifiers);
    }

    @PostMapping(path = "/users/{userId}/removeAuthorities", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeUserAuthorities(
            @PathVariable("domainId") String domainId,
            @PathVariable("userId") String userId,
            @RequestBody @Valid @Size(min = 1, message = "authority identifiers required")
                    List<String> authorityIdentifiers
    ) {
        authorityManagementService.revokeUserAuthorities(domainId, userId, authorityIdentifiers);
    }
}
