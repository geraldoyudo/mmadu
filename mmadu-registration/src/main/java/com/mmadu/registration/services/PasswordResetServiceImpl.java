package com.mmadu.registration.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mmadu.registration.exceptions.DomainNotFoundException;
import com.mmadu.registration.models.PasswordResetFlowConfiguration;
import com.mmadu.registration.models.PasswordResetRequestForm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class PasswordResetServiceImpl implements PasswordResetService {
    private DomainFlowConfigurationService domainFlowConfigurationService;
    private WebClient userServiceClient;

    @Autowired
    public void setDomainFlowConfigurationService(DomainFlowConfigurationService domainFlowConfigurationService) {
        this.domainFlowConfigurationService = domainFlowConfigurationService;
    }

    @Autowired
    @Qualifier("userService")
    public void setUserServiceClient(WebClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void initiatePasswordReset(String domainId, PasswordResetRequestForm requestForm) {
        log.info("Resetting password for domain {} {}", domainId, requestForm);
        PasswordResetFlowConfiguration configuration = domainFlowConfigurationService.findByDomainId(domainId)
                .orElseThrow(DomainNotFoundException::new).getPasswordReset();
        if (configuration == null) {
            configuration = new PasswordResetFlowConfiguration();
        }
        Flux.fromIterable(configuration.getUserFields())
                .map(propertyName -> this.convertToQuery(propertyName, requestForm))
                .flatMap(query -> this.queryForSingleUser(domainId, query))
                .take(1)
                .subscribe(this::createPasswordRequestTokenForUser);
    }

    private String convertToQuery(String property, PasswordResetRequestForm form) {
        return String.format("%s equals %s", property, form.getUser());
    }

    private Mono<String> queryForSingleUser(String domainId, String query) {
        // GET /domains/test-app/users/search?page=0&size=10&query=%28country+equals+%27Nigeria%27%29+and+%28favourite-color+equals+%27blue%27%29 HTTP/1.1
        return userServiceClient.get()
                .uri(uriBuilder -> uriBuilder.path("/domains/")
                        .path(domainId)
                        .path("/users/search")
                        .queryParam("size", 2)
                        .queryParam("query", query)
                        .build()
                )
                .retrieve()
                .bodyToMono(UserResponse.class)
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex))
                .flatMap(this::ensureSingle);
    }

    private Mono<String> ensureSingle(UserResponse response) {
        if (response.getContent() == null || response.getContent().size() != 1) {
            return Mono.empty();
        } else {
            return Mono.just(response.getContent().get(0).getId());
        }
    }

    private void createPasswordRequestTokenForUser(String userId) {
        log.info("Creating password request token for user {}", userId);
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserResponse {
        private List<User> content;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        private String id;
    }
}
