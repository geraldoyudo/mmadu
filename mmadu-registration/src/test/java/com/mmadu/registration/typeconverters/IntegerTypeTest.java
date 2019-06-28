package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IntegerTypeTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private FieldType fieldType;
    private IntegerType integerType;

    @Before
    public void setUp() {
        fieldType = new FieldType();
        fieldType.setType("integer");
        fieldType.setMin("10");
        fieldType.setMax("100");
        integerType = new IntegerType(fieldType);
    }

    @Test
    public void givenNonNumberShouldThrowValidationException() throws Exception {
        expectedException.expect(FieldValidationException.class);
        integerType.validate("dsfa");
    }

    @Test
    public void givenSmallNumberShouldThrowValidationException() throws Exception {
        expectedException.expect(FieldValidationException.class);
        expectedException.expectMessage("Value is less than minimum value");
        integerType.validate("2");
    }

    @Test
    public void givenLargeNumberShouldThrowValidationException() throws Exception {
        expectedException.expect(FieldValidationException.class);
        expectedException.expectMessage("Value is greater than minimum value");
        integerType.validate("102");
    }

    @Test
    public void givenNumberWithinRangeValidationShouldPass() throws Exception {
        integerType.validate("80");
    }
}