package com.mmadu.registration.repositories;

import com.mmadu.registration.entities.RegistrationProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RegistrationProfileRepository extends MongoRepository<RegistrationProfile, String> {
    Optional<RegistrationProfile> findByDomainId(@Param("domainId") String domainId);

    boolean existsByDomainId(@Param("domainId") String domainId);
}
