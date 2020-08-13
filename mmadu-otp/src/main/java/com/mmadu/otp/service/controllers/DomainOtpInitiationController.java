package com.mmadu.otp.service.controllers;

import com.mmadu.otp.service.config.DomainOtpConfigurationList;
import com.mmadu.otp.service.populators.DomainPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/domains")
public class DomainOtpInitiationController {
    private DomainPopulator domainPopulator;

    @Autowired
    public void setDomainPopulator(DomainPopulator domainPopulator) {
        this.domainPopulator = domainPopulator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('domain_otp.initialize')")
    public void registerDomains(@RequestBody @Valid List<DomainOtpConfigurationList.DomainItem> domains) {
        domainPopulator.initializeDomains(domains);
    }
}
