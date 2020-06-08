package com.mmadu.service.repositories;


import com.mmadu.service.entities.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends MongoRepository<UserRole, String> {

}
