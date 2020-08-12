package com.mmadu.otp.service.repositories;

import com.mmadu.otp.service.entities.DomainOtpConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DomainOtpConfigurationRepository extends MongoRepository<DomainOtpConfiguration, String> {

    Optional<DomainOtpConfiguration> findByDomainId(@Param("domainId") String domainId);

    boolean existsByDomainId(@Param("domainId") String domainId);
}
