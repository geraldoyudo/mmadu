package com.mmadu.registration.providers;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.repositories.FieldRepository;
import com.mmadu.registration.repositories.FieldTypeRepository;
import com.mmadu.registration.utils.VelocityEngineConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.mmadu.registration.utils.EntityUtils.*;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = FormFieldsGeneratorImpl.class)
@Import(VelocityEngineConfig.class)
public class FormFieldsGeneratorImplTest {
    @MockBean
    private FieldRepository fieldRepository;
    @MockBean
    private FieldTypeRepository fieldTypeRepository;
    @MockBean
    private FieldMarkupGenerator fieldMarkupGenerator;

    private Field nameField, classField;
    private FieldType textFieldType;

    @Autowired
    private FormFieldsGenerator formFieldsGenerator;

    @Before
    public void setUp() {
        textFieldType = createFieldType("1");
        nameField = createField("1", "name", "name", "1");
        classField = createField("2", "class", "class", "1");
        doReturn(asList(nameField, classField)).when(fieldRepository).findByDomainId(DOMAIN_ID);
        doReturn(Optional.of(textFieldType)).when(fieldTypeRepository).findById("1");
        doReturn("<markup-name></markup-name>").when(fieldMarkupGenerator).resolveField(nameField, textFieldType);
        doReturn("<markup-class></markup-class>").when(fieldMarkupGenerator).resolveField(classField, textFieldType);
    }

    @Test
    public void generateFormFieldsForDomain() {
        assertThat(formFieldsGenerator.generateFormFieldsForDomain(DOMAIN_ID).replaceAll("\\s", ""),
                equalTo("<markup-name></markup-name><markup-class></markup-class>"));
    }
}