package com.mmadu.identity.controllers.configuration;

import com.mmadu.identity.config.DomainIdentityConfigurationList;
import com.mmadu.identity.populators.DomainIdentityPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/admin/domains")
public class DomainIdentityManagementController {
    private DomainIdentityPopulator domainIdentityPopulator;

    @Autowired
    public void setDomainIdentityPopulator(DomainIdentityPopulator domainIdentityPopulator) {
        this.domainIdentityPopulator = domainIdentityPopulator;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('domain_identity.initialize')")
    @ResponseStatus(HttpStatus.CREATED)
    public void initializeDomains(
            @RequestBody @Size(min = 1)
                    List<DomainIdentityConfigurationList.DomainIdentityItem> domains) {
        domainIdentityPopulator.initializeDomains(domains);
    }
}
