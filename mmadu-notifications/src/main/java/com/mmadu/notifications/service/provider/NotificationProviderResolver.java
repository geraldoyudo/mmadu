package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.endpoint.NotificationProvider;
import com.mmadu.notifications.endpoint.models.NotificationMessage;
import reactor.core.publisher.Mono;

public interface NotificationProviderResolver {

    Mono<String> getProviderForMessage(NotificationMessage message, String domainId, String profileId);
}
