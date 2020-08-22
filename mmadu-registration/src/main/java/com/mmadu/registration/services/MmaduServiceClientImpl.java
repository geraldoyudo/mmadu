package com.mmadu.registration.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    @Override
    public void updateUserPassword(String domainId, String userId, String password) {
        userServiceClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/domains/")
                                .path(domainId)
                                .path("/users/")
                                .path(userId)
                                .path("/resetPassword")
                                .build()
                ).body(BodyInserters.fromValue(Map.of("newPassword", password)))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    @Override
    public Mono<String> queryForSingleUser(String domainId, String query) {

        return userServiceClient.get()
                .uri(uriBuilder -> uriBuilder.path("/domains/")
                        .path(domainId)
                        .path("/users/search")
                        .queryParam("size", "{size}")
                        .queryParam("query", "{query}")
                        .build(2, query)
                )
                .retrieve()
                .bodyToMono(PasswordResetServiceImpl.UserResponse.class)
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex))
                .flatMap(this::ensureSingle);
    }

    private Mono<String> ensureSingle(PasswordResetServiceImpl.UserResponse response) {
        if (response.getContent() == null || response.getContent().size() != 1) {
            return Mono.empty();
        } else {
            return Mono.just(response.getContent().get(0).getId());
        }
    }

    @Override
    public Mono<String> getUserProperty(String domainId, String userId, String propertyName) {
        return userServiceClient.get()
                .uri(uriBuilder -> uriBuilder.path("/domains/")
                        .path(domainId)
                        .path("/users/")
                        .path(userId)
                        .build()
                )
                .retrieve()
                .bodyToMono(HashMap.class)
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex))
                .map(user -> user.get(propertyName))
                .filter(Objects::nonNull)
                .map(Object::toString)
                .onErrorResume(NullPointerException.class,
                        ex -> Mono.empty());
    }

    @Override
    public Mono<Void> setPropertyValidationState(String domainId, String userId, String propertyName, boolean valid) {
        return userServiceClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/domains/")
                                .path(domainId)
                                .path("/users/")
                                .path(userId)
                                .path("/setPropertyValidationState")
                                .build()
                ).body(BodyInserters.fromValue(Map.of(
                        "propertyName", propertyName,
                        "valid", valid
                )))
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    @Override
    public Mono<Boolean> containsByQuery(String domainId, String query) {
        return userServiceClient.get()
                .uri(uriBuilder -> uriBuilder.path("/domains/")
                        .path(domainId)
                        .path("/users/search")
                        .queryParam("size", "{size}")
                        .queryParam("query", "{query}")
                        .build(1, query)
                )
                .retrieve()
                .bodyToMono(PasswordResetServiceImpl.UserResponse.class)
                .map(userResponse -> !userResponse.getContent().isEmpty())
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getRawStatusCode() == 404 ? Mono.just(false) : Mono.error(ex));
    }
}
