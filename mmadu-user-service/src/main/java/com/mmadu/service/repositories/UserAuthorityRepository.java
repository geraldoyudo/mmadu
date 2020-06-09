package com.mmadu.service.repositories;


import com.mmadu.service.entities.UserAuthority;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAuthorityRepository extends MongoRepository<UserAuthority, String> {

    boolean existsByDomainIdAndUserIdAndAuthorityId(
            @Param("domainId") String domainId,
            @Param("userId") String userId,
            @Param("authorityId") String authorityId);

    void deleteByDomainIdAndUserIdAndAuthorityId(
            @Param("domainId") String domainId,
            @Param("userId") String userId,
            @Param("authorityId") String authorityId);

    void deleteByDomainIdAndAuthorityId(
            @Param("domainId") String domainId,
            @Param("authorityId") String authorityId);

    List<UserAuthority> findByDomainIdAndUserId(String domainId, String userId);
}
