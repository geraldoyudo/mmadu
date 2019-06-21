package com.mmadu.registration.repositories;

import com.mmadu.registration.entities.FieldType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;

import static com.mmadu.registration.utils.EntityUtils.createFieldType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringRunner.class)
@DataMongoTest
public class FieldTypeRepositoryTest {
    @Autowired
    private FieldTypeRepository fieldTypeRepository;

    @Test
    public void fieldTypeShouldBeAbleToBeCreated() {
        FieldType type = createFieldType("1");
        fieldTypeRepository.save(type);
    }

    @Test
    public void fieldTypeWithTheSameIdShouldNotBeSaved() {
        fieldTypeRepository.save(createFieldType("1"));
        fieldTypeRepository.save(createFieldType("1"));
        assertThat(fieldTypeRepository.findById("1").get(), notNullValue());
    }

    @Test(expected = DuplicateKeyException.class)
    public void fieldTypeWithTheSameNameShouldNotBeSaved() {
        FieldType fieldType = createFieldType("1");
        FieldType fieldType1 = createFieldType("2");
        fieldType.setName(fieldType1.getName());
        fieldTypeRepository.save(fieldType);
        fieldTypeRepository.save(fieldType1);
    }
}