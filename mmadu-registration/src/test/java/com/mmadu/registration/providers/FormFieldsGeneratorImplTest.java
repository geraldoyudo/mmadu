package com.mmadu.registration.providers;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.repositories.FieldRepository;
import com.mmadu.registration.repositories.FieldTypeRepository;
import com.mmadu.registration.repositories.RegistrationProfileRepository;
import com.mmadu.registration.utils.VelocityEngineConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.mmadu.registration.utils.EntityUtils.*;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = FormFieldsGeneratorImpl.class)
@Import(VelocityEngineConfig.class)
public class FormFieldsGeneratorImplTest {
    public static final String PROFILE_ID = "2222";
    public static final List<String> FIELD_CODES = asList("name", "class");
    @MockBean
    private FieldRepository fieldRepository;
    @MockBean
    private FieldTypeRepository fieldTypeRepository;
    @MockBean
    private FieldMarkupGenerator fieldMarkupGenerator;
    @MockBean
    private RegistrationProfileRepository registrationProfileRepository;

    @Mock
    private RegistrationProfile profile;

    private Field nameField, classField;
    private FieldType textFieldType;

    @Autowired
    private FormFieldsGenerator formFieldsGenerator;

    @BeforeEach
    void setUp() {
        textFieldType = createFieldType("1");
        textFieldType.setScript("a-script");
        nameField = createField("1", "name", "name", "1");
        classField = createField("2", "class", "class", "1");
        when(registrationProfileRepository.findById(PROFILE_ID)).thenReturn(Optional.of(profile));
        when(profile.getFields()).thenReturn(FIELD_CODES);
        when(profile.getDomainId()).thenReturn(DOMAIN_ID);
        doReturn(asList(nameField, classField)).when(fieldRepository).findByDomainIdAndCodeIn(DOMAIN_ID, FIELD_CODES);
        doReturn(Optional.of(textFieldType)).when(fieldTypeRepository).findById("1");
        doReturn("<markup-name></markup-name>").when(fieldMarkupGenerator)
                .resolveField(nameField, textFieldType);
        doReturn("<markup-class></markup-class>").when(fieldMarkupGenerator)
                .resolveField(classField, textFieldType);
    }

    @Test
    void generateFormFieldsForDomain() {
        assertThat(formFieldsGenerator.generateFormFieldsForProfile(PROFILE_ID).replaceAll("\\s", ""),
                equalTo("<markup-name></markup-name><markup-class></markup-class><script>a-script</script>"));
    }
}