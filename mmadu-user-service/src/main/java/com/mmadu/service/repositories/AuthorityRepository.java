package com.mmadu.service.repositories;


import com.mmadu.service.entities.Authority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorityRepository extends MongoRepository<Authority, String> {

    Page<Authority> findByDomainId(@Param("domainId") String domainId, Pageable p);

    Optional<Authority> findByDomainIdAndIdentifier(@Param("domainId") String domainId,
                                                    @Param("identifier") String identifier);

    boolean existsByDomainIdAndIdentifier(@Param("domainId") String domainId,
                                          @Param("identifier") String identifier);

    void deleteByDomainIdAndIdentifier(@Param("domainId") String domainId,
                                       @Param("identifier") String identifier);

    List<Authority> findByDomainIdAndIdentifierIn(@Param("domainId") String domainId,
                                                  @Param("identifiers") List<String> identifiers);
}
