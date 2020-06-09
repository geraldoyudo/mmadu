package com.mmadu.service.repositories;


import com.mmadu.service.entities.Role;
import com.mmadu.service.entities.RoleAuthority;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleAuthorityRepository extends MongoRepository<RoleAuthority, String> {

    boolean existsByDomainIdAndRoleIdAndAuthorityId(@Param("domainId") String domainId,
                                                    @Param("roleId") String roleId,
                                                    @Param("authorityId") String authorityId);

    Optional<RoleAuthority> findByDomainIdAndRoleIdAndAuthorityId(@Param("domainId") String domainId,
                                                                  @Param("roleId") String roleId,
                                                                  @Param("authorityId") String authorityId);

    void deleteByDomainIdAndRoleId(@Param("domainId") String domainId, @Param("roleId") String roleId);


    List<RoleAuthority> findByDomainIdAndRoleIn(
            String domainId,
            List<Role> roles);
}
