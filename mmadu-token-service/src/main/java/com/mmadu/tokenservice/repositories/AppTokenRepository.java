package com.mmadu.tokenservice.repositories;

import com.mmadu.tokenservice.entities.AppToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppTokenRepository extends MongoRepository<AppToken, String> {

}
