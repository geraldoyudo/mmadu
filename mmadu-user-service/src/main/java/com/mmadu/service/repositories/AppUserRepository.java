package com.mmadu.service.repositories;


import com.mmadu.service.entities.AppUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends MongoRepository<AppUser, String> {

    Optional<AppUser> findByUsernameAndDomainId(@Param("username") String username,
            @Param("domainId") String domain);

    Page<AppUser> findByDomainId(@Param("domainId") String domain, Pageable pageDetail);
}
