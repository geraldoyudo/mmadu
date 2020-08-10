package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.endpoint.models.NotificationUser;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<NotificationUser> findByUserIdAndDomain(String userId, String domainId);
}
