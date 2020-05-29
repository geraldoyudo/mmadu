package com.mmadu.identity.providers.users;

import com.mmadu.identity.models.users.Domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class DomainServiceImpl implements DomainService {
    @Autowired
    @Qualifier("userService")
    private WebClient userServiceClient;

    @Override
    public Mono<Domain> findById(String id) {
        return userServiceClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/appDomains/")
                        .path(id)
                        .build()
                )
                .retrieve()
                .bodyToMono(Domain.class);
    }
}
