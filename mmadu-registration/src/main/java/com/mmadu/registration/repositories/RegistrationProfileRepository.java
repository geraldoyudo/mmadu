package com.mmadu.registration.repositories;

import com.mmadu.registration.entities.RegistrationProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistrationProfileRepository extends MongoRepository<RegistrationProfile, String> {
    List<RegistrationProfile> findByDomainId(@Param("domainId") String domainId);

    Optional<RegistrationProfile> findByDomainIdAndCode(@Param("domainId") String domainId, @Param("code") String code);

    boolean existsByDomainId(@Param("domainId") String domainId);
}
