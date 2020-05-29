package com.mmadu.identity.repositories;

import com.mmadu.identity.entities.ClientInstance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientInstanceRepository extends MongoRepository<ClientInstance, String> {

    List<ClientInstance> findByClientId(@Param("clientId") String clientId);
}
