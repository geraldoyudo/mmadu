package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.service.entities.DomainNotificationConfiguration;
import com.mmadu.notifications.service.models.GenericUserEvent;
import com.mmadu.notifications.service.repositories.DomainNotificationConfigurationRepository;
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

@Component
@RepositoryEventHandler
public class ScheduledNotificationMessageSubscriptionHandler {
    private ScheduledNotificationManager scheduledNotificationManager;
    private ScheduledNotificationMessageHandlerFactory messageHandlerFactory;
    private DomainNotificationConfigurationRepository domainNotificationConfigurationRepository;
    private final Map<String, Disposable> subscription = new HashMap<>();

    @Autowired
    public void setMessageHandlerFactory(ScheduledNotificationMessageHandlerFactory messageHandlerFactory) {
        this.messageHandlerFactory = messageHandlerFactory;
    }

    @Autowired
    public void setScheduledNotificationManager(ScheduledNotificationManager scheduledNotificationManager) {
        this.scheduledNotificationManager = scheduledNotificationManager;
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
        Disposable disposable = scheduledNotificationManager.getSubscriberForDomain(domainId)
                .receiveEventsFor(GenericUserEvent.class)
                .subscribe(handler::handleEvent);
        subscription.put(domainId, disposable);
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
