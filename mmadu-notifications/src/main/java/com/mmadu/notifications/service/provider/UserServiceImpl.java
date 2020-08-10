package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.endpoint.models.NotificationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserServiceImpl implements UserService {
    private WebClient userServiceClient;

    @Autowired
    @Qualifier("userService")
    public void setUserServiceClient(WebClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public Mono<NotificationUser> findByUserIdAndDomain(String userId, String domainId) {
        return userServiceClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/domains/")
                                .path(domainId)
                                .path("/users/")
                                .path(userId)
                                .build()
                )
                .retrieve()
                .bodyToMono(NotificationUser.class);
    }
}
