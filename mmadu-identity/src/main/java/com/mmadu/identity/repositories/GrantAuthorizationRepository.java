package com.mmadu.identity.repositories;

import com.mmadu.identity.entities.GrantAuthorization;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface GrantAuthorizationRepository extends MongoRepository<GrantAuthorization, String> {

    @Query("{ 'data.code' : ?0 }")
    Optional<GrantAuthorization> findByAuthorizationCode(@Param("code") String code);

    @Query("{ 'clientIdentifier': ?0, 'data.code' : ?1 }")
    Optional<GrantAuthorization> findByClientIdentifierAndAuthorizationCode(
            @Param("clientId") String clientId,
            @Param("code") String code);

    List<GrantAuthorization> findByClientIdentifierAndGrantTypeAndActive(
            @Param("clientId") String clientId,
            @Param("grantType") String grantType,
            @Param("active") Boolean active
    );

    @DeleteQuery("{ $or: [ {expiryTime: { $lt: ?0}}, {revokedTime: { $lt: ?0}}, {expired: true}, {revoked: true} ] }")
    void deleteExpiredAndRevokedAuthorizations(ZonedDateTime time);
}
