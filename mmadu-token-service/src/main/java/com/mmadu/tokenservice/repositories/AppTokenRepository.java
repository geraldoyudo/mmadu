package com.mmadu.tokenservice.repositories;

import com.mmadu.tokenservice.entities.AppToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface AppTokenRepository extends MongoRepository<AppToken, String>{

}
