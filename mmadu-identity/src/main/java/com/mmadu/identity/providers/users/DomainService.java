package com.mmadu.identity.providers.users;

import com.mmadu.identity.models.users.Domain;
import reactor.core.publisher.Mono;

public interface DomainService {
    Mono<Domain> findById(String id);
}
