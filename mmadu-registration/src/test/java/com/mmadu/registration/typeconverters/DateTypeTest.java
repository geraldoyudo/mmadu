package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.mmadu.registration.typeconverters.FieldUtils.fieldTypeWithPattern;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class DateTypeTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private DateType dateType;

    @Before
    public void setUp() {
        FieldType fieldType = fieldTypeWithPattern("dd-MM-yyyy");
        fieldType.setMin("01-01-2000");
        fieldType.setMax("01-01-2020");
        dateType = new DateType(fieldType);
    }

    @Test
    public void convertToObject() throws FieldConversionException {
        Object date = dateType.convertToObject("01-01-2001");
        assertThat(date, notNullValue());
    }

    @Test
    public void validate() throws FieldValidationException {
        dateType.validate("01-01-2001");
    }

    @Test
    public void givenNonNumberShouldThrowValidationException() throws Exception {
        expectedException.expect(FieldValidationException.class);
        dateType.validate("dsfa");
    }

    @Test
    public void givenSmallDateShouldThrowValidationException() throws Exception {
        expectedException.expect(FieldValidationException.class);
        expectedException.expectMessage("Value is earlier than minimum date");
        dateType.validate("01-01-1890");
    }

    @Test
    public void givenLargeLargeShouldThrowValidationException() throws Exception {
        expectedException.expect(FieldValidationException.class);
        expectedException.expectMessage("Value is later than maximum date");
        dateType.validate("01-01-2030");
    }

}