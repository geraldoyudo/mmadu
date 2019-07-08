package com.mmadu.registration.repositories;

import com.mmadu.registration.entities.Field;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FieldRepository extends MongoRepository<Field, String> {
    List<Field> findByDomainId(@Param("domainId") String domainId);

    List<Field> findByFieldTypeId(@Param("fieldTypeId") String fieldTypeId);
}
