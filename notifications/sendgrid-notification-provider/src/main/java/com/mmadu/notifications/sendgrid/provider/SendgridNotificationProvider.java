package com.mmadu.notifications.sendgrid.provider;

import com.mmadu.notifications.endpoint.NotificationProvider;
import com.mmadu.notifications.endpoint.models.NotificationMessage;
import com.mmadu.notifications.endpoint.models.NotificationProviderConfiguration;
import reactor.core.publisher.Mono;

public class SendgridNotificationProvider implements NotificationProvider {

    @Override
    public Mono<Void> process(NotificationMessage message, NotificationProviderConfiguration configuration) {
        return Mono.empty();
    }

    @Override
    public boolean supportsMessage(NotificationMessage message) {
        return message.getType().equalsIgnoreCase("email");
    }
}
