package com.mmadu.service.repositories;

import com.mmadu.service.entities.AppToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppTokenRepository extends MongoRepository<AppToken, String>{

}
