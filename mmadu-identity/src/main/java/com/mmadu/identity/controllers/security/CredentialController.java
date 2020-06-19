package com.mmadu.identity.controllers.security;

import com.mmadu.identity.exceptions.DomainNotFoundException;
import com.mmadu.identity.models.security.CredentialGenerationRequest;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import com.mmadu.identity.services.security.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/admin/domains/{domainId}/credentials")
public class CredentialController {
    private DomainIdentityConfigurationService domainIdentityConfigurationService;
    private CredentialService credentialService;

    @Autowired
    public void setDomainIdentityConfigurationService(DomainIdentityConfigurationService domainIdentityConfigurationService) {
        this.domainIdentityConfigurationService = domainIdentityConfigurationService;
    }

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @InitBinder
    public void checkDomain(@PathVariable("domainId") String domainId, WebDataBinder binder) {
        domainIdentityConfigurationService.findByDomainId(domainId)
                .orElseThrow(DomainNotFoundException::new);
    }

    @PreAuthorize("hasAuthority('credential.create')")
    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String createCredential(@PathVariable("domainId") String domainId,
                                   @RequestBody @Valid CredentialGenerationRequest request) {
        return credentialService.generateCredentialForDomain(domainId, request);
    }

    @PreAuthorize("hasAuthority('credential.read')")
    @GetMapping(path = "/{credentialId}/verificationKey", produces = "text/plain")
    public String viewCredentialVerificationKey(@PathVariable("domainId") String domainId,
                                                          @PathVariable("credentialId") String credentialId) {
        return credentialService.getCredentialVerificationKey(domainId, credentialId);
    }
}
