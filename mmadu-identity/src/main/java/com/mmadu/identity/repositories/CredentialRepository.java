package com.mmadu.identity.repositories;

import com.mmadu.identity.entities.credentials.Credential;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CredentialRepository extends MongoRepository<Credential, String> {
}
