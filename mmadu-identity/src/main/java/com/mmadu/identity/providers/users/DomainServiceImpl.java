package com.mmadu.identity.providers.users;

import com.mmadu.identity.models.users.Domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Component
public class DomainServiceImpl implements DomainService {
    @Autowired
    @Qualifier("userService")
    private WebClient userServiceClient;

    @Override
    @Cacheable("domains")
    public Optional<Domain> findById(String id) {
        try {
            return userServiceClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/appDomains/")
                            .path(id)
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(Domain.class)
                    .blockOptional();
        }catch (WebClientResponseException.NotFound ex){
            return Optional.empty();
        }
    }
}
