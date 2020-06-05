package com.mmadu.service.repositories;


import com.mmadu.service.entities.Group;
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

    List<UserGroup> findByDomainIdAndUserId(String domainId, String userId);


    boolean existsByDomainIdAndUserIdAndGroupId(String domainId,
                                                String externalId,
                                                String groupIdentifier);

    Page<UserGroup> findByDomainIdAndGroupIn(String domainId, List<Group> groups, Pageable p);

    Page<UserGroup> findByDomainIdAndUserIdAndGroupIdIn(String domainId, String userId, List<String> groupIds, Pageable p);

    boolean existsByDomainIdAndUserIdAndGroupIn(String domainId,
                                                String externalId,
                                                List<Group> groups);

    void deleteByDomainIdAndUserIdAndGroupId(String domainId,
                                             String externalId,
                                             String groupIdentifier);

}
