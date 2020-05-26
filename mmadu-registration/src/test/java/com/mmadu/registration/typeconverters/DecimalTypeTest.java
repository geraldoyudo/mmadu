package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DecimalTypeTest {
    private FieldType fieldType;
    private DecimalType decimalType;

    @BeforeEach
    void setUp() {
        fieldType = new FieldType();
        fieldType.setType("integer");
        fieldType.setMin("10");
        fieldType.setMax("100");
        decimalType = new DecimalType(fieldType);
    }

    @Test
    void givenNonNumberShouldThrowValidationException() throws Exception {
        assertThrows(FieldValidationException.class, () -> decimalType.validate("dsfa"));
    }

    @Test
    void givenSmallNumberShouldThrowValidationException() throws Exception {
        Exception ex = assertThrows(FieldValidationException.class, () -> decimalType.validate("2.0"));
        assertEquals("Value is less than minimum value", ex.getMessage());
    }

    @Test
    void givenLargeNumberShouldThrowValidationException() throws Exception {
        Exception ex = assertThrows(FieldValidationException.class, () -> decimalType.validate("102.32323"));
        assertEquals("Value is greater than minimum value", ex.getMessage());
    }

    @Test
    void givenNumberWithinRangeValidationShouldPass() throws Exception {
        decimalType.validate("59.32323");
    }
}