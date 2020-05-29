package com.mmadu.identity.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class RestResourceConfig {

    public static final String DOMAIN_AUTH_TOKEN_HEADER = "domain-auth-token";

    @Bean
    @Qualifier("userService")
    public WebClient userServiceClient(@Value("${mmadu.userService.url}") String userServiceUrl,
                                       @Value("${mmadu.domainKey}") String domainKey) {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(DOMAIN_AUTH_TOKEN_HEADER, domainKey)
                .baseUrl(userServiceUrl)
                .build();
    }
}
