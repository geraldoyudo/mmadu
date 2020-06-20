package com.mmadu.service.controllers;

import com.mmadu.service.config.DomainConfigurationList;
import com.mmadu.service.populators.DomainPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/domains")
public class DomainManagementController {
    private DomainPopulator domainPopulator;

    @Autowired
    public void setDomainPopulator(DomainPopulator domainPopulator) {
        this.domainPopulator = domainPopulator;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('domain.initialize')")
    @ResponseStatus(HttpStatus.CREATED)
    public void setUpDomains(@RequestBody @Size(min = 1)
                                     List<DomainConfigurationList.DomainItem> domainItems) {
        domainPopulator.initializeDomains(domainItems);
    }
}
