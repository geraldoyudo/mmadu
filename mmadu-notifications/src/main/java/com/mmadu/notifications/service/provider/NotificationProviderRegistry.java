package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.endpoint.NotificationProvider;
import com.mmadu.notifications.endpoint.models.NotificationMessage;

import java.util.Optional;

public interface NotificationProviderRegistry {

    Optional<NotificationProvider> getProvider(String providerId, NotificationMessage message);
}
