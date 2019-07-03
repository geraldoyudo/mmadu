package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.mmadu.registration.typeconverters.FieldUtils.fieldTypeWithPattern;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class DateTimeTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private DateTimeType dateTimeType;

    @Before
    public void setUp() {
        FieldType type = fieldTypeWithPattern("dd-MM-yyyy.HH:mm:ss");
        type.setMin("01-01-2016.10:00:00");
        type.setMax("01-01-2016.12:00:00");
        dateTimeType = new DateTimeType(type);
    }

    @Test
    public void convertToObject() throws FieldConversionException {
        Object date = dateTimeType.convertToObject("01-01-2001.11:02:03");
        assertThat(date, notNullValue());
    }

    @Test
    public void validate() throws FieldValidationException {
        dateTimeType.validate("01-01-2016.11:00:00");
    }


    @Test
    public void givenSmallTimeShouldThrowValidationException() throws Exception {
        expectedException.expect(FieldValidationException.class);
        expectedException.expectMessage("Value is earlier than minimum datetime");
        dateTimeType.validate("01-01-2016.01:00:00");
    }

    @Test
    public void givenLargeNumberShouldThrowValidationException() throws Exception {
        expectedException.expect(FieldValidationException.class);
        expectedException.expectMessage("Value is later than maximum datetime");
        dateTimeType.validate("01-02-2016.11:00:00");
    }
}