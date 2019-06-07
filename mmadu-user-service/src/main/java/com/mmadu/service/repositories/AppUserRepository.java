package com.mmadu.service.repositories;


import com.mmadu.service.entities.AppUser;
import com.mmadu.service.models.DomainIdObject;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends MongoRepository<AppUser, String> {

    boolean existsByUsernameAndDomainId(@Param("username") String username,
                                                @Param("domainId") String domain);

    boolean existsByExternalIdAndDomainId(@Param("externalId") String username,
                                        @Param("domainId") String domain);

    Optional<AppUser> findByUsernameAndDomainId(@Param("username") String username,
            @Param("domainId") String domain);

    Page<AppUser> findByDomainId(@Param("domainId") String domain, Pageable pageDetail);

    @Query(value = "{ _id: ?0 }", fields = "{ domainId: 1, _id: 0}")
    Optional<DomainIdObject> findDomainIdForUser(@Param("id") String id);

    Optional<AppUser> findByDomainIdAndExternalId(@Param("domainId")String domainId,
                                                  @Param("externalId")String externalId);
}
