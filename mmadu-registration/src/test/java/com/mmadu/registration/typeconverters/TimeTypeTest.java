package com.mmadu.registration.typeconverters;

import org.junit.Test;

import static com.mmadu.registration.typeconverters.FieldUtils.fieldTypeWithPattern;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class TimeTypeTest {
    private final TimeType timeType = new TimeType(fieldTypeWithPattern("HH:mm:ss"));

    @Test
    public void convertToObject() throws FieldConversionException {
        Object time = timeType.convertToObject("12:01:02");
        assertThat(time, notNullValue());
    }

    @Test
    public void validate() throws FieldValidationException {
        timeType.validate("12:01:02");
    }

    @Test(expected = FieldValidationException.class)
    public void wrongTimeShouldThrowError() throws FieldValidationException {
        timeType.validate("12:01");
    }
}