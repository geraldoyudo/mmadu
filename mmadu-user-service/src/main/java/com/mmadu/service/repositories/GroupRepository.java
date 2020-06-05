package com.mmadu.service.repositories;


import com.mmadu.service.entities.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {

    Page<Group> findByDomainId(@Param("domainId") String domainId, Pageable p);

    Optional<Group> findByDomainIdAndIdentifier(@Param("domainId") String domainId,
                                                @Param("identifier") String identifier);

    boolean existsByDomainIdAndIdentifier(@Param("domainId") String domainId,
                                          @Param("identifier") String identifier);
}
