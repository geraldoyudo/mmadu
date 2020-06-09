package com.mmadu.service.repositories;


import com.mmadu.service.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {

    Page<Role> findByDomainId(@Param("domainId") String domainId, Pageable p);

    Optional<Role> findByDomainIdAndIdentifier(@Param("domainId") String domainId,
                                               @Param("identifier") String identifier);

    boolean existsByDomainIdAndIdentifier(@Param("domainId") String domainId,
                                          @Param("identifier") String identifier);

    List<Role> findByDomainIdAndIdentifierIn(@Param("domainId") String domainId,
                                             @Param("identifiers") List<String> identifiers);
}
