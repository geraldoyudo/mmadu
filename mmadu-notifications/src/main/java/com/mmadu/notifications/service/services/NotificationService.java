package com.mmadu.notifications.service.services;

import com.mmadu.notifications.service.models.SendNotificationMessageRequest;
import com.mmadu.notifications.service.models.SendUserNotificationMessageRequest;
import reactor.core.publisher.Mono;

public interface NotificationService {

    Mono<Void> sendToUser(SendUserNotificationMessageRequest request);

    Mono<Void> send(SendNotificationMessageRequest request);
}
