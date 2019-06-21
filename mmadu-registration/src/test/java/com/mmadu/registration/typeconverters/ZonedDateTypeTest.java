package com.mmadu.registration.typeconverters;

import org.junit.Test;

import static com.mmadu.registration.typeconverters.FieldUtils.fieldTypeWithPattern;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ZonedDateTypeTest {
    private final DateTimeType dateTimeType = new DateTimeType(fieldTypeWithPattern("dd-MM-yyyy.HH:mm:ss"));

    @Test
    public void convertToObject() throws FieldConversionException {
        Object date = dateTimeType.convertToObject("01-01-2001.11:02:03");
        assertThat(date, notNullValue());
    }

    @Test
    public void validate() throws FieldValidationException {
        dateTimeType.validate("01-01-2001.11:02:03");
    }
}