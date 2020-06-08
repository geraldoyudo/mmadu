package com.mmadu.service.repositories;


import com.mmadu.service.entities.RoleAuthority;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleAuthorityRepository extends MongoRepository<RoleAuthority, String> {

}
