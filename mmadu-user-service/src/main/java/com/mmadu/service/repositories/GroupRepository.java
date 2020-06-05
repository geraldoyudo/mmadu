package com.mmadu.service.repositories;


import com.mmadu.service.entities.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {

    Page<Group> findByDomainId(String domainId, Pageable p);
}
