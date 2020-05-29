package com.mmadu.identity.repositories;

import com.mmadu.identity.entities.ClientInstance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientInstanceRepository extends MongoRepository<ClientInstance, String> {

    Optional<ClientInstance> findByDomainIdAndIdentifier(@Param("domainId") String domainId,
                                                         @Param("identifier") String identifier);

    List<ClientInstance> findByDomainIdAndClientId(@Param("domainId") String domainId,
                                                   @Param("clientId") String clientId);
}
