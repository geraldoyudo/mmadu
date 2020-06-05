package com.mmadu.service.repositories;


import com.mmadu.service.entities.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends MongoRepository<UserGroup, String> {

    Page<UserGroup> findByDomainId(String domainId, Pageable p);

    Page<UserGroup> findByUserId(String userId, Pageable p);

    List<UserGroup> findByUserId(String userId);

    Page<UserGroup> findByDomainIdAndUserExternalId(String domainId, String userExternalId, Pageable p);

    List<UserGroup> findByDomainIdAndUserExternalId(String domainId, String userExternalId);

    Page<UserGroup> findByDomainIdAndUserUsername(String domainId, String username, Pageable p);

    boolean existsByDomainIdAndUserExternalIdAndGroupIdentifier(String domainId,
                                                                String externalId,
                                                                String groupIdentifier);

    Page<UserGroup> findByDomainIdAndGroupIdIn(String domainId, List<String> groupIds, Pageable p);

}
