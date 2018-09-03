package com.mmadu.service.repositories;


import com.mmadu.service.entities.AppUser;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends MongoRepository<AppUser, String> {

    Optional<AppUser> findByUsernameAndDomainId(String username, String domain);
}
