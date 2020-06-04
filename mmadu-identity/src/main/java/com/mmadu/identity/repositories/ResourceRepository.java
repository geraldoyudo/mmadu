package com.mmadu.identity.repositories;

import com.mmadu.identity.entities.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends MongoRepository<Resource, String> {

    Optional<Resource> findByDomainIdAndIdentifier(@Param("domainId") String domainId,
                                                   @Param("identifier") String identifier);

    boolean existsByDomainIdAndIdentifier(@Param("domainId") String domainId,
                                          @Param("identifier") String identifier);

    int countByDomainIdAndIdentifierIn(@Param("domainId") String domainId,
                                       @Param("identifier") List<String> identifier);

    Page<Resource> findByDomainId(@Param("domainId") String domainId, Pageable p);
}
