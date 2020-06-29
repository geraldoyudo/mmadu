package com.mmadu.service.repositories;


import com.mmadu.service.entities.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends MongoRepository<UserRole, String> {

    void deleteByDomainIdAndRoleId(
            @Param("domainId") String domainId,
            @Param("roleId") String roleId
    );

    boolean existsByDomainIdAndUserIdAndRoleId(
            @Param("domainId") String domainId,
            @Param("userId") String userId,
            @Param("roleId") String roleId
    );

    void deleteByDomainIdAndUserIdAndRoleId(
            @Param("domainId") String domainId,
            @Param("userId") String userId,
            @Param("roleId") String roleId
    );

    List<UserRole> findByDomainIdAndUserId(
            @Param("domainId") String domainId,
            @Param("userId") String userId
    );
}
