package com.mmadu.identity.repositories;

import com.mmadu.identity.entities.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, String> {
}
