package com.mmadu.notifications.service.services;

import com.mmadu.notifications.service.models.SendNotificationMessageRequest;
import reactor.core.publisher.Mono;

public interface NotificationService {

    Mono<Void> send(SendNotificationMessageRequest request);
}
