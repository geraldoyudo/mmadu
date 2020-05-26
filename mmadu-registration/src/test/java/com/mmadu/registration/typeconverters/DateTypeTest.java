package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.mmadu.registration.typeconverters.FieldUtils.fieldTypeWithPattern;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DateTypeTest {
    private DateType dateType;

    @BeforeEach
    void setUp() {
        FieldType fieldType = fieldTypeWithPattern("dd-MM-yyyy");
        fieldType.setMin("01-01-2000");
        fieldType.setMax("01-01-2020");
        dateType = new DateType(fieldType);
    }

    @Test
    void convertToObject() throws FieldConversionException {
        Object date = dateType.convertToObject("01-01-2001");
        assertThat(date, notNullValue());
    }

    @Test
    void validate() throws FieldValidationException {
        dateType.validate("01-01-2001");
    }

    @Test
    void givenNonNumberShouldThrowValidationException() throws Exception {
        assertThrows(FieldValidationException.class, () -> dateType.validate("dsfa"));
    }

    @Test
    void givenSmallDateShouldThrowValidationException() throws Exception {
        Exception ex = assertThrows(FieldValidationException.class, () -> dateType.validate("01-01-1890"));
        assertEquals("Value is earlier than minimum date", ex.getMessage());
    }

    @Test
    void givenLargeLargeShouldThrowValidationException() throws Exception {
        Exception ex = assertThrows(FieldValidationException.class, () -> dateType.validate("01-01-2030"));
        assertEquals("Value is later than maximum date", ex.getMessage());
    }

}