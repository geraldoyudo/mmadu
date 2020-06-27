package com.mmadu.registration.controllers;

import com.mmadu.registration.config.DomainFlowConfiguration;
import com.mmadu.registration.populators.DomainFlowPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public void registerDomains(@RequestBody @Valid DomainFlowConfiguration configuration){
        domainFlowPopulator.initializeDomainEnvironment(configuration);
    }
}
