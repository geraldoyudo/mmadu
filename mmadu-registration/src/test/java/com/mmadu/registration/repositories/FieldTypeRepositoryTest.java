package com.mmadu.registration.repositories;

import com.mmadu.registration.entities.FieldType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;

import static com.mmadu.registration.utils.EntityUtils.createFieldType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
public class FieldTypeRepositoryTest {
    @Autowired
    private FieldTypeRepository fieldTypeRepository;

    @Test
    void fieldTypeShouldBeAbleToBeCreated() {
        FieldType type = createFieldType("1");
        fieldTypeRepository.save(type);
    }

    @Test
    void fieldTypeWithTheSameIdShouldNotBeSaved() {
        fieldTypeRepository.save(createFieldType("1"));
        fieldTypeRepository.save(createFieldType("1"));
        assertThat(fieldTypeRepository.findById("1").get(), notNullValue());
    }

    @Test
    @Disabled("Test is failing unexpectedly. Please fix")
    void fieldTypeWithTheSameNameShouldNotBeSaved() {
        FieldType fieldType = createFieldType("1");
        FieldType fieldType1 = createFieldType("2");
        fieldType.setName(fieldType1.getName());
        fieldTypeRepository.save(fieldType);
        assertThrows(DuplicateKeyException.class, () -> fieldTypeRepository.save(fieldType1));
    }
}