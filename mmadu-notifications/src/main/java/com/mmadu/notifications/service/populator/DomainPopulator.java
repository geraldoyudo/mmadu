package com.mmadu.notifications.service.populator;

import com.mmadu.notifications.service.config.DomainNotificationConfigurationList;
import com.mmadu.notifications.service.entities.DomainNotificationConfiguration;
import com.mmadu.notifications.service.entities.NotificationProfile;
import com.mmadu.notifications.service.entities.ProviderConfiguration;
import com.mmadu.notifications.service.entities.ScheduledUserNotificationMessage;
import com.mmadu.notifications.service.repositories.DomainNotificationConfigurationRepository;
import com.mmadu.notifications.service.repositories.NotificationProfileRepository;
import com.mmadu.notifications.service.repositories.ProviderConfigurationRepository;
import com.mmadu.notifications.service.repositories.ScheduledUserNotificationMessageRepository;
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
    private DomainNotificationConfigurationList domainNotificationConfigurationList;
    private DomainNotificationConfigurationRepository domainNotificationConfigurationRepository;
    private ProviderConfigurationRepository providerConfigurationRepository;
    private NotificationProfileRepository notificationProfileRepository;
    private ScheduledUserNotificationMessageRepository scheduledUserNotificationMessageRepository;

    @Autowired
    public void setDomainNotificationConfigurationList(DomainNotificationConfigurationList domainNotificationConfigurationList) {
        this.domainNotificationConfigurationList = domainNotificationConfigurationList;
    }

    @Autowired
    public void setDomainNotificationConfigurationRepository(DomainNotificationConfigurationRepository domainNotificationConfigurationRepository) {
        this.domainNotificationConfigurationRepository = domainNotificationConfigurationRepository;
    }

    @Autowired
    public void setProviderConfigurationRepository(ProviderConfigurationRepository providerConfigurationRepository) {
        this.providerConfigurationRepository = providerConfigurationRepository;
    }

    @Autowired
    public void setNotificationProfileRepository(NotificationProfileRepository notificationProfileRepository) {
        this.notificationProfileRepository = notificationProfileRepository;
    }

    @Autowired
    public void setScheduledNotificationMessageRepository(ScheduledUserNotificationMessageRepository scheduledUserNotificationMessageRepository) {
        this.scheduledUserNotificationMessageRepository = scheduledUserNotificationMessageRepository;
    }

    @Transactional
    @EventListener(ContextRefreshedEvent.class)
    public void setUpDomainEntities() {
        List<DomainNotificationConfigurationList.DomainItem> unInitializedDomains =
                Optional.ofNullable(domainNotificationConfigurationList.getDomains())
                        .orElse(Collections.emptyList())
                        .stream()
                        .filter(domainIdentityItem -> !domainNotificationConfigurationRepository.existsByDomainId(domainIdentityItem.getDomainId()))
                        .collect(Collectors.toList());
        if (!unInitializedDomains.isEmpty()) {
            doInitializeDomains(unInitializedDomains);
        }

    }

    @Transactional
    public void initializeDomains(List<DomainNotificationConfigurationList.DomainItem> domainItems) {
        doInitializeDomains(domainItems);
    }

    private void doInitializeDomains(List<DomainNotificationConfigurationList.DomainItem> domainItems) {
        for (DomainNotificationConfigurationList.DomainItem item : domainItems) {
            initializeDomain(item);
        }
    }

    private void initializeDomain(DomainNotificationConfigurationList.DomainItem domainItem) {
        DomainNotificationConfiguration configuration = domainItem.toEntity();
        domainNotificationConfigurationRepository.save(configuration);
        saveProviderConfigurations(domainItem);
        saveNotificationProfiles(domainItem);
        saveScheduledNotificationMessages(domainItem);
    }

    private void saveProviderConfigurations(DomainNotificationConfigurationList.DomainItem domainItem) {
        List<ProviderConfiguration> providerConfigurations = domainItem.getProviderConfigurations()
                .stream()
                .map(pc -> pc.toEntity(domainItem.getDomainId()))
                .collect(Collectors.toList());
        domainItem.getProfiles()
                .stream()
                .flatMap(p -> p.getProviderConfigurations().stream().map(pc -> pc.toEntity(domainItem.getDomainId(), p.getProfileId())))
                .forEach(providerConfigurations::add);
        providerConfigurationRepository.saveAll(providerConfigurations);
    }

    private void saveNotificationProfiles(DomainNotificationConfigurationList.DomainItem domainItem) {
        List<NotificationProfile> profiles = domainItem.getProfiles()
                .stream()
                .map(p -> p.toEntity(domainItem.getDomainId()))
                .collect(Collectors.toList());
        notificationProfileRepository.saveAll(profiles);
    }

    private void saveScheduledNotificationMessages(DomainNotificationConfigurationList.DomainItem domainItem) {
        List<ScheduledUserNotificationMessage> messages = Optional.ofNullable(domainItem.getNotificationMessages())
                .orElse(Collections.emptyList())
                .stream()
                .map(m -> m.toEntity(domainItem.getDomainId()))
                .collect(Collectors.toList());
        scheduledUserNotificationMessageRepository.saveAll(messages);
    }
}
