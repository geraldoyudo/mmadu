package com.mmadu.registration.typeconverters;

import org.junit.Test;

import static com.mmadu.registration.typeconverters.FieldUtils.fieldTypeWithPattern;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class DateTypeTest {
    private final DateType dateType = new DateType(fieldTypeWithPattern("dd-MM-yyyy"));

    @Test
    public void convertToObject() throws FieldConversionException {
        Object date = dateType.convertToObject("01-01-2001");
        assertThat(date, notNullValue());
    }

    @Test
    public void validate() throws FieldValidationException {
        dateType.validate("01-01-2001");
    }
}