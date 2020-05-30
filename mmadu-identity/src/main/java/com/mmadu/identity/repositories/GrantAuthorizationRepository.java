package com.mmadu.identity.repositories;

import com.mmadu.identity.entities.GrantAuthorization;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GrantAuthorizationRepository extends MongoRepository<GrantAuthorization, String> {

}
