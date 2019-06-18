package com.mmadu.registration.repositories;

import com.mmadu.registration.entities.Field;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FieldRepository extends MongoRepository<Field, String> {
}
