package com.mmadu.identity.services.user;

import com.mmadu.identity.models.user.MmaduUser;
import com.mmadu.identity.models.user.MmaduUserImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationServiceException;
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
@Slf4j
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
                            .path("/domains/")
                            .path(domainId)
                            .path("/users/load")
                            .queryParam("username", username)
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(MmaduUserImpl.class)
                    .map(user -> this.setDomain(user, domainId))
                    .cast(MmaduUser.class)
                    .blockOptional();
        } catch (WebClientResponseException.NotFound ex) {
            return Optional.empty();
        }
    }

    private MmaduUserImpl setDomain(MmaduUserImpl user, String domainId) {
        user.setDomainId(domainId);
        return user;
    }

    @Override
    public void authenticate(String domainId, String username, String password) {
        Optional<String> status;
        try {
            status = userServiceClient.post()
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
                    .doOnError(ex -> log.error("An error occurred", ex))
                    .map(result -> (String) result.getOrDefault("status", "USERNAME_INVALID"))
                    .blockOptional();
        } catch (Exception ex) {
            log.error("An unexpected error occurred", ex);
            throw new AuthenticationServiceException("Login error", ex);
        }

        if (status.isPresent()) {
            String statusString = status.get();
            switch (statusString) {
                case "AUTHENTICATED":
                    return;
                case "USERNAME_INVALID":
                    throw new UsernameNotFoundException("username invalid");
                case "PASSWORD_INVALID":
                    throw new BadCredentialsException("password invalid");
            }
        }
        throw new BadCredentialsException("could not authenticate");
    }
}
