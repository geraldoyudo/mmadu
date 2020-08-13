package com.mmadu.registration.repositories;

import com.mmadu.registration.entities.ValidationContext;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ValidationContextRepository extends MongoRepository<ValidationContext, String> {

    Optional<ValidationContext> findByDomainIdAndUserIdAndKey(@Param("domainId") String domainId,
                                                              @Param("userId") String userId,
                                                              @Param("key") String key);

    void deleteByDomainIdAndUserIdAndKey(@Param("domainId") String domainId,
                                                              @Param("userId") String userId,
                                                              @Param("key") String key);
}
