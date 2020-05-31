package com.mmadu.identity.providers.users;

import com.mmadu.identity.models.users.MmaduUser;
import com.mmadu.identity.models.users.MmaduUserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class MmaduUserServiceImpl implements MmaduUserService {
    private WebClient userServiceClient;

    @Autowired
    @Qualifier("userService")
    public void setUserServiceClient(WebClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public Optional<MmaduUser> loadUserByUsernameAndDomainId(String username, String domainId) {
        try {
            return userServiceClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/appUsers/search/findByUsernameAndDomainId")
                            .queryParam("username", username)
                            .queryParam("domainId", domainId)
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(MmaduUserImpl.class)
                    .cast(MmaduUser.class)
                    .blockOptional();
        } catch (WebClientResponseException.NotFound ex) {
            return Optional.empty();
        }
    }

    @Override
    public void authenticate(String domainId, String username, String password) {
        Optional<String> status = userServiceClient.post()
                .uri(uriBuilder -> uriBuilder.path("/domains/")
                        .path(domainId)
                        .path("/authenticate")
                        .build()
                )
                .body(BodyInserters.fromValue(
                        Map.of(
                                "username", username,
                                "password", password
                        )
                ))
                .retrieve()
                .bodyToMono(HashMap.class)
                .map(result -> (String) result.getOrDefault("status", "USERNAME_INVALID"))
                .blockOptional();
        if (status.isPresent()) {
            String statusString = status.get();
            switch (statusString) {
                case "AUTHENTICATED": return;
                case "USERNAME_INVALID": throw new UsernameNotFoundException("username invalid");
                case "PASSWORD_INVALID": throw  new BadCredentialsException("password invalid");
            }
        }
        throw new BadCredentialsException("could not authenticate");
    }
}
