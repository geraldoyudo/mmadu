package com.mmadu.identity.repositories;

import com.mmadu.identity.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientRepository extends MongoRepository<Client, String> {

    Optional<Client> findByDomainIdAndCode(@Param("domainId") String domainId, @Param("code") String code);

    Page<Client> findByDomainId(@Param("domainId") String domainId, Pageable p);
}
