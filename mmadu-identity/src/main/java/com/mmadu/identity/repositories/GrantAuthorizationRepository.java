package com.mmadu.identity.repositories;

import com.mmadu.identity.entities.GrantAuthorization;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GrantAuthorizationRepository extends MongoRepository<GrantAuthorization, String> {

    @Query("{ 'data.code' : ?0 }")
    Optional<GrantAuthorization> findByAuthorizationCode(@Param("code") String code);
}
