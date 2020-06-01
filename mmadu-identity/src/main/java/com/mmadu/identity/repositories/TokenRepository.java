package com.mmadu.identity.repositories;

import com.mmadu.identity.entities.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token, String> {
}
