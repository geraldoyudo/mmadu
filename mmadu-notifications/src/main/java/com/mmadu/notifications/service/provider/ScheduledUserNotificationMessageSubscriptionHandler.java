package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.service.entities.DomainNotificationConfiguration;
import com.mmadu.notifications.service.models.DomainInitializedEvent;
import com.mmadu.notifications.service.models.GenericUserEvent;
import com.mmadu.notifications.service.repositories.DomainNotificationConfigurationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RepositoryEventHandler
public class ScheduledUserNotificationMessageSubscriptionHandler {
    private ScheduledUserNotificationManager scheduledUserNotificationManager;
    private ScheduledNotificationMessageHandlerFactory messageHandlerFactory;
    private DomainNotificationConfigurationRepository domainNotificationConfigurationRepository;
    private final Map<String, Disposable> subscription = new HashMap<>();

    @Autowired
    public void setMessageHandlerFactory(ScheduledNotificationMessageHandlerFactory messageHandlerFactory) {
        this.messageHandlerFactory = messageHandlerFactory;
    }

    @Autowired
    public void setScheduledNotificationManager(ScheduledUserNotificationManager scheduledUserNotificationManager) {
        this.scheduledUserNotificationManager = scheduledUserNotificationManager;
    }

    @Autowired
    public void setDomainNotificationConfigurationRepository(DomainNotificationConfigurationRepository domainNotificationConfigurationRepository) {
        this.domainNotificationConfigurationRepository = domainNotificationConfigurationRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void initializeSubscribers() {
        domainNotificationConfigurationRepository.findAll()
                .forEach(this::initializeSubscriberForDomain);
    }

    private void initializeSubscriberForDomain(DomainNotificationConfiguration domainNotificationConfiguration) {
        String domainId = domainNotificationConfiguration.getDomainId();
        ScheduledNotificationMessageHandler handler = messageHandlerFactory.getHandlerForDomain(domainId);
        Disposable disposable = scheduledUserNotificationManager.getSubscriberForDomain(domainId)
                .receiveEventsFor(GenericUserEvent.class)
                .subscribe(handler::handleUserEvent);
        subscription.put(domainId, disposable);
    }

    @EventListener
    public void handleDomainInitialized(DomainInitializedEvent event){
        domainNotificationConfigurationRepository.findByDomainId(event.getDomainId())
                .ifPresentOrElse(this::initializeSubscriberForDomain, () -> log.warn("DomainNotificationConfiguration with domain not found {}", event.getDomainId()));
    }

    @HandleAfterCreate
    public void subscribeOnDomainCreation(DomainNotificationConfiguration configuration) {
        initializeSubscriberForDomain(configuration);
    }

    @HandleAfterDelete
    public void unsubscribeOnDomainDeletion(DomainNotificationConfiguration configuration) {
        Optional.ofNullable(subscription.get(configuration.getDomainId()))
                .ifPresent(Disposable::dispose);
    }
}
