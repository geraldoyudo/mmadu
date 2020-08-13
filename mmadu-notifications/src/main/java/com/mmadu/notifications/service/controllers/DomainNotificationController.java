package com.mmadu.notifications.service.controllers;

import com.mmadu.notifications.service.config.DomainNotificationConfigurationList;
import com.mmadu.notifications.service.populator.DomainPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/domains")
public class DomainNotificationController {
    private DomainPopulator domainPopulator;

    @Autowired
    public void setDomainPopulator(DomainPopulator domainPopulator) {
        this.domainPopulator = domainPopulator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('domain_notification.initialize')")
    public void registerDomains(@RequestBody @Valid List<DomainNotificationConfigurationList.DomainItem> domains) {
        domainPopulator.initializeDomains(domains);
    }
}
