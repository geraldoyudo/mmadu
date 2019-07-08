package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.mmadu.registration.typeconverters.FieldUtils.fieldTypeWithPattern;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class TimeTypeTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private TimeType timeType;

    @Before
    public void setUp() {
        FieldType fieldType = fieldTypeWithPattern("HH:mm:ss");
        fieldType.setMin("10:00:00");
        fieldType.setMax("20:00:00");
        timeType = new TimeType(fieldType);
    }

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

    @Test
    public void givenSmallTimeShouldThrowValidationException() throws Exception {
        expectedException.expect(FieldValidationException.class);
        expectedException.expectMessage("Value is earlier than minimum time");
        timeType.validate("09:00:00");
    }

    @Test
    public void givenLargeNumberShouldThrowValidationException() throws Exception {
        expectedException.expect(FieldValidationException.class);
        expectedException.expectMessage("Value is later than maximum time");
        timeType.validate("21:00:00");
    }
}