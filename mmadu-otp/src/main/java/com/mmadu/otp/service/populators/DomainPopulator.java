package com.mmadu.otp.service.populators;

import com.mmadu.otp.service.config.DomainOtpConfigurationList;
import com.mmadu.otp.service.entities.DomainOtpConfiguration;
import com.mmadu.otp.service.entities.OtpProfile;
import com.mmadu.otp.service.repositories.DomainOtpConfigurationRepository;
import com.mmadu.otp.service.repositories.OtpProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DomainPopulator {
    private DomainOtpConfigurationList domainOtpConfigurationList;
    private DomainOtpConfigurationRepository domainOtpConfigurationRepository;
    private OtpProfileRepository otpProfileRepository;

    @Autowired
    public void setOtpProfileRepository(OtpProfileRepository otpProfileRepository) {
        this.otpProfileRepository = otpProfileRepository;
    }

    @Autowired
    public void setDomainOtpConfigurationRepository(DomainOtpConfigurationRepository domainOtpConfigurationRepository) {
        this.domainOtpConfigurationRepository = domainOtpConfigurationRepository;
    }

    @Autowired
    public void setDomainOtpConfigurationList(DomainOtpConfigurationList domainOtpConfigurationList) {
        this.domainOtpConfigurationList = domainOtpConfigurationList;
    }

    @Transactional
    @EventListener(ContextRefreshedEvent.class)
    public void setUpDomainEntities() {
        List<DomainOtpConfigurationList.DomainItem> unInitializedDomains =
                Optional.ofNullable(domainOtpConfigurationList.getDomains())
                        .orElse(Collections.emptyList())
                        .stream()
                        .filter(domainIdentityItem -> !domainOtpConfigurationRepository.existsByDomainId(domainIdentityItem.getDomainId()))
                        .collect(Collectors.toList());
        if (!unInitializedDomains.isEmpty()) {
            doInitializeDomains(unInitializedDomains);
        }
    }

    @Transactional
    public void initializeDomains(List<DomainOtpConfigurationList.DomainItem> domainItems) {
        doInitializeDomains(domainItems);
    }

    private void doInitializeDomains(List<DomainOtpConfigurationList.DomainItem> domainItems) {
        for (DomainOtpConfigurationList.DomainItem item : domainItems) {
            initializeDomain(item);
        }
    }

    private void initializeDomain(DomainOtpConfigurationList.DomainItem domainItem) {
        DomainOtpConfiguration configuration = domainItem.toEntity();
        domainOtpConfigurationRepository.save(configuration);
        saveOtpProfiles(domainItem);
    }

    private void saveOtpProfiles(DomainOtpConfigurationList.DomainItem domainItem) {
        List<OtpProfile> profiles = domainItem.getProfiles().stream()
                .map(p -> p.toEntity(domainItem.getDomainId()))
                .collect(Collectors.toList());
        otpProfileRepository.saveAll(profiles);
    }
}
