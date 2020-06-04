package com.mmadu.identity.services.domain;

import com.mmadu.identity.models.domain.Domain;

import java.util.Optional;

public interface DomainService {
    Optional<Domain> findById(String id);
}
