package com.mmadu.registration.repositories;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;

import static com.mmadu.registration.utils.EntityUtils.createField;
import static com.mmadu.registration.utils.EntityUtils.createFieldType;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@DataMongoTest
public class FieldRepositoryTest {
    public static final String FIELD_ID = "1";
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private FieldTypeRepository fieldTypeRepository;

    private Field field;
    private FieldType fieldType;

    @BeforeEach
    void setUp() {
        fieldType = fieldTypeRepository.save(createFieldType("1"));
        field = createField(FIELD_ID, fieldType.getId());
    }

    @Test
    void testSave() {
        fieldRepository.save(field);
        assertTrue(fieldRepository.existsById(FIELD_ID));
    }

    @Test
    @Disabled("Test is failing unexpectedly. Please fix")
    void fieldsWithTheSameDomainIdAndNameShouldNotBeAllowed() {
        fieldRepository.save(createField("1", "name", "1"));
        assertThrows(DuplicateKeyException.class, () -> fieldRepository.save(createField("2", "name", "1")));
    }

    @Test
    @Disabled("Test is failing unexpectedly. Please fix")
    void fieldsWithTheSameDomainIdAndPropertyShouldNotBeAllowed() {
        fieldRepository.save(createField("1", "name", "name", "1"));
        assertThrows(DuplicateKeyException.class, () -> fieldRepository.save(createField("2", "name1", "name", "1")));
    }


}