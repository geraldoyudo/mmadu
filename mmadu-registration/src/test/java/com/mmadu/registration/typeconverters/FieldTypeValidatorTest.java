package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.models.UserForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class FieldTypeValidatorTest {
    private static final String PROPERTY = "name";
    public static final String PROPERTY_VALUE = "propy";

    @Mock
    private AbstractFieldTypeConverter fieldTypeConverter;
    @Mock
    private Field field;
    private FieldTypeValidator validator;

    @BeforeEach
    void setUp() {
        validator = new FieldTypeValidator(fieldTypeConverter, field);
        doReturn(PROPERTY).when(field).getProperty();
    }

    @Test
    void ifRequiredAndFieldNotPresentShouldRegisterError() {
        doReturn(true).when(field).isRequired();
        UserForm userForm = new UserForm();
        Errors errors = new BeanPropertyBindingResult(userForm, "user");
        validator.validate(userForm, errors);
        assertTrue(errors.hasErrors());
    }

    @Test
    void ifRequiredAndFieldPresentShouldNotRegisterError() {
        doReturn(true).when(field).isRequired();
        UserForm userForm = new UserForm();
        userForm.set(PROPERTY, "propy");
        Errors errors = new BeanPropertyBindingResult(userForm, "user");
        validator.validate(userForm, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    void ifFieldPresentAndInvalidItShouldHaveErrors() throws Exception {
        doReturn(true).when(field).isRequired();
        UserForm userForm = new UserForm();
        userForm.set(PROPERTY, PROPERTY_VALUE);
        doThrow(new FieldValidationException()).when(fieldTypeConverter).validate(PROPERTY_VALUE);
        Errors errors = new BeanPropertyBindingResult(userForm, "user");
        validator.validate(userForm, errors);
        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors("properties[name]"));
        assertThat(errors.getAllErrors().size(), equalTo(1));
        assertThat(errors.getAllErrors().get(0).getCode(), equalTo("name.invalid"));
        assertThat(errors.getAllErrors().get(0).getDefaultMessage(), equalTo("invalid field"));
    }
}