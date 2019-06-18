package com.mmadu.registration.repositories;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;

import static com.mmadu.registration.utils.EntityUtils.createField;
import static com.mmadu.registration.utils.EntityUtils.createFieldType;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataMongoTest
public class FieldRepositoryTest {
    public static final String FIELD_ID = "1";
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private FieldTypeRepository fieldTypeRepository;

    private Field field;
    private FieldType fieldType;

    @Before
    public void setUp() {
        fieldType = fieldTypeRepository.save(createFieldType("1"));
        field = createField(FIELD_ID, fieldType.getId());
    }

    @Test
    public void testSave() {
        fieldRepository.save(field);
        assertTrue(fieldRepository.existsById(FIELD_ID));
    }

    @Test(expected = DuplicateKeyException.class)
    public void fieldsWithTheSameDomainIdAndNameShouldNotBeAllowed() {
        fieldRepository.save(createField("1", "name", "1"));
        fieldRepository.save(createField("2", "name", "1"));
    }

    @Test(expected = DuplicateKeyException.class)
    public void fieldsWithTheSameDomainIdAndPropertyShouldNotBeAllowed() {
        fieldRepository.save(createField("1", "name", "name", "1"));
        fieldRepository.save(createField("2", "name1", "name", "1"));
    }


}