package com.mmadu.registration.services;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface MmaduUserServiceClient {

    void addUsers(String domainId, Map<String, Object> user);

    void updateUserPassword(String domainId, String userId, String password);

    Mono<String> queryForSingleUser(String domainId, String query);

    Mono<String> getUserProperty(String domainId, String userId, String propertyName);

    Mono<Void> setPropertyValidationState(String domainId, String userId, String propertyName, boolean valid);

    Mono<Boolean> containsByQuery(String domainId, String query);
}
