package com.mmadu.identity.repositories;

import com.mmadu.identity.entities.keys.Key;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface KeyRepository extends MongoRepository<Key, String> {

}
