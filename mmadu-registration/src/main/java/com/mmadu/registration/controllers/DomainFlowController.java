package com.mmadu.registration.controllers;

import com.mmadu.registration.config.DomainFlowConfigurationList;
import com.mmadu.registration.populators.DomainFlowPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/domains")
public class DomainFlowController {
    private DomainFlowPopulator domainFlowPopulator;

    @Autowired
    public void setDomainFlowPopulator(DomainFlowPopulator domainFlowPopulator) {
        this.domainFlowPopulator = domainFlowPopulator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('domain_flow.initialize')")
    public void registerDomains(@RequestBody @Valid DomainFlowConfigurationList configuration){
        domainFlowPopulator.initializeDomainEnvironment(configuration);
    }
}
