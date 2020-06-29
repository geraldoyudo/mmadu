package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.mmadu.registration.typeconverters.FieldUtils.fieldTypeWithPattern;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TimeTypeTest {
    private TimeType timeType;

    @BeforeEach
    void setUp() {
        FieldType fieldType = fieldTypeWithPattern("HH:mm:ss");
        fieldType.setMin("10:00:00");
        fieldType.setMax("20:00:00");
        timeType = new TimeType(fieldType);
    }

    @Test
    void convertToObject() throws FieldConversionException {
        Object time = timeType.convertToObject("12:01:02");
        assertThat(time, notNullValue());
    }

    @Test
    void validate() throws FieldValidationException {
        timeType.validate("12:01:02");
    }

    @Test
    void wrongTimeShouldThrowError() throws FieldValidationException {
        assertThrows(FieldValidationException.class, () -> timeType.validate("12:01"));
    }

    @Test
    void givenSmallTimeShouldThrowValidationException() throws Exception {
        Exception ex = assertThrows(FieldValidationException.class, () -> timeType.validate("09:00:00"));
        assertEquals("Value is earlier than minimum time", ex.getMessage());
    }

    @Test
    void givenLargeNumberShouldThrowValidationException() throws Exception {
        Exception ex = assertThrows(FieldValidationException.class, () -> timeType.validate("21:00:00"));
        assertEquals("Value is later than maximum time", ex.getMessage());
    }
}