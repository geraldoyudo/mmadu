package com.mmadu.service.repositories;

import com.mmadu.service.entities.AppDomain;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppDomainRepository extends MongoRepository<AppDomain, String> {
    boolean existsById(String domainId);
}
