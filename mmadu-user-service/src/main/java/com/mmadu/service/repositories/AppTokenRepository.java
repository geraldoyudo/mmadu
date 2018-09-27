package com.mmadu.service.repositories;

import com.mmadu.service.entities.AppToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface AppTokenRepository extends MongoRepository<AppToken, String>{

}
