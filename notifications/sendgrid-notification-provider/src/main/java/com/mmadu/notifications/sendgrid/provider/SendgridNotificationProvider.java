package com.mmadu.notifications.sendgrid.provider;

import com.mmadu.notifications.endpoint.NotificationProvider;
import com.mmadu.notifications.endpoint.models.NotificationMessage;
import com.mmadu.notifications.endpoint.models.NotificationProviderConfiguration;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class SendgridNotificationProvider implements NotificationProvider {

    @Override
    public Mono<Void> process(NotificationMessage message, NotificationProviderConfiguration configuration) {
        return Mono.fromRunnable(
                () -> log.info("Sendgrid: processing {} {}", message, configuration)
        ).then();
    }

    @Override
    public boolean supportsMessage(NotificationMessage message) {
        return message.getType().equalsIgnoreCase("email");
    }
}
