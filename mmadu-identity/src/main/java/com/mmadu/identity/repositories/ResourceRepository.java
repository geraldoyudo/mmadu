package com.mmadu.identity.repositories;

import com.mmadu.identity.entities.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ResourceRepository extends MongoRepository<Resource, String> {

    Optional<Resource> findByDomainIdAndIdentifier(@Param("domainId") String domainId,
                                                   @Param("identifier") String identifier);
}
