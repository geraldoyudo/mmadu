package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.models.UserForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class FieldTypeValidatorTest {
    private static final String PROPERTY = "name";
    public static final String PROPERTY_VALUE = "propy";

    @Mock
    private AbstractFieldTypeConverter fieldTypeConverter;
    @Mock
    private Field field;
    private FieldTypeValidator validator;

    @Before
    public void setUp(){
        validator = new FieldTypeValidator(fieldTypeConverter, field);
        doReturn(PROPERTY).when(field).getProperty();
    }

    @Test
    public void ifRequiredAndFieldNotPresentShouldRegisterError() {
        doReturn(true).when(field).isRequired();
        UserForm userForm = new UserForm();
        Errors errors = new BeanPropertyBindingResult(userForm, "user");
        validator.validate(userForm, errors);
        assertTrue(errors.hasErrors());
    }

    @Test
    public void ifRequiredAndFieldPresentShouldNotRegisterError() {
        doReturn(true).when(field).isRequired();
        UserForm userForm = new UserForm();
        userForm.set(PROPERTY, "propy");
        Errors errors = new BeanPropertyBindingResult(userForm, "user");
        validator.validate(userForm, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void ifFieldPresentAndInvalidItShouldHaveErrors() throws Exception {
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