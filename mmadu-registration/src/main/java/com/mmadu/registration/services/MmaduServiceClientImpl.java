package com.mmadu.registration.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class MmaduServiceClientImpl implements MmaduUserServiceClient {
    private String authKey;
    private String userServiceUrl;
    private RestTemplate restTemplate;

    @Value("${mmadu.domainKey}")
    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    @Value("${mmadu.userService.url}")
    public void setUserServiceUrl(String userServiceUrl) {
        this.userServiceUrl = userServiceUrl;
    }

    @Autowired
    private void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void addUsers(String domainId, Map<String, Object> user) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("domain-auth-token", authKey);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(user, headers);
        restTemplate
                .exchange(UriComponentsBuilder.fromHttpUrl(userServiceUrl)
                                .path("/domains/")
                                .path(domainId)
                                .path("/users")
                                .build().toUriString(),
                        HttpMethod.POST, entity, String.class);
    }
}
