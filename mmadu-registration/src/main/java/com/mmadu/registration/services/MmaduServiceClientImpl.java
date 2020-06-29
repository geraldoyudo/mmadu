package com.mmadu.registration.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class MmaduServiceClientImpl implements MmaduUserServiceClient {
    private WebClient userServiceClient;

    @Autowired
    @Qualifier("userService")
    public void setUserServiceClient(WebClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void addUsers(String domainId, Map<String, Object> user) {
        userServiceClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/domains/")
                                .path(domainId)
                                .path("/users")
                                .build()
                ).body(BodyInserters.fromValue(user))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
