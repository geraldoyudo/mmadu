package com.mmadu.otp.service.repositories;

import com.mmadu.otp.service.entities.OtpProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OtpProfileRepository extends MongoRepository<OtpProfile, String> {

    Optional<OtpProfile> findByDomainIdAndIdentifier(@Param("domainId") String domainId,
                                                     @Param("identifier") String identifier);
}
