package com.mmadu.identity.repositories;

import com.mmadu.identity.entities.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScopeRepository extends MongoRepository<Scope, String> {

    Optional<Scope> findByDomainIdAndCode(@Param("domainId") String domainId, @Param("code") String code);

    List<Scope> findByDomainId(@Param("domainId") String domainId);

    List<Scope> findByDomainIdAndCodeIn(@Param("domainId") String domainId,
                                        @Param("scopeCodes") List<String> scopeCodes);

    int countByDomainIdAndCodeIn(@Param("domainId") String domainId,
                                 @Param("scopeCodes") List<String> scopeCodes);
}
