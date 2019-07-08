package com.mmadu.registration.repositories;

import com.mmadu.registration.entities.FieldType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FieldTypeRepository extends MongoRepository<FieldType, String> {
}
