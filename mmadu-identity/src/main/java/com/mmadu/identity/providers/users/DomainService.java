package com.mmadu.identity.providers.users;

import com.mmadu.identity.models.users.Domain;

import java.util.Optional;

public interface DomainService {
    Optional<Domain> findById(String id);
}
