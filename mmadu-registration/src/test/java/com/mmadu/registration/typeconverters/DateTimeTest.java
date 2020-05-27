package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.mmadu.registration.typeconverters.FieldUtils.fieldTypeWithPattern;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DateTimeTest {
    private DateTimeType dateTimeType;

    @BeforeEach
    void setUp() {
        FieldType type = fieldTypeWithPattern("dd-MM-yyyy.HH:mm:ss");
        type.setMin("01-01-2016.10:00:00");
        type.setMax("01-01-2016.12:00:00");
        dateTimeType = new DateTimeType(type);
    }

    @Test
    void convertToObject() throws FieldConversionException {
        Object date = dateTimeType.convertToObject("01-01-2001.11:02:03");
        assertThat(date, notNullValue());
    }

    @Test
    void validate() throws FieldValidationException {
        dateTimeType.validate("01-01-2016.11:00:00");
    }


    @Test
    void givenSmallTimeShouldThrowValidationException() throws Exception {
        Exception ex = assertThrows(FieldValidationException.class, () -> dateTimeType.validate("01-01-2016.01:00:00"));
        assertEquals("Value is earlier than minimum datetime", ex.getMessage());
    }

    @Test
    void givenLargeNumberShouldThrowValidationException() throws Exception {
        Exception ex = assertThrows(FieldValidationException.class, () -> dateTimeType.validate("01-02-2016.11:00:00"));
        assertEquals("Value is later than maximum datetime", ex.getMessage());
    }
}