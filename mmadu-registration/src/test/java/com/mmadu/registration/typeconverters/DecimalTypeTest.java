package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DecimalTypeTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private FieldType fieldType;
    private DecimalType decimalType;

    @Before
    public void setUp() {
        fieldType = new FieldType();
        fieldType.setType("integer");
        fieldType.setMin("10");
        fieldType.setMax("100");
        decimalType = new DecimalType(fieldType);
    }

    @Test
    public void givenNonNumberShouldThrowValidationException() throws Exception {
        expectedException.expect(FieldValidationException.class);
        decimalType.validate("dsfa");
    }

    @Test
    public void givenSmallNumberShouldThrowValidationException() throws Exception {
        expectedException.expect(FieldValidationException.class);
        expectedException.expectMessage("Value is less than minimum value");
        decimalType.validate("2.0");
    }

    @Test
    public void givenLargeNumberShouldThrowValidationException() throws Exception {
        expectedException.expect(FieldValidationException.class);
        expectedException.expectMessage("Value is greater than minimum value");
        decimalType.validate("102.32323");
    }

    @Test
    public void givenNumberWithinRangeValidationShouldPass() throws Exception {
        decimalType.validate("59.32323");
    }
}