package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegerTypeTest {
    private FieldType fieldType;
    private IntegerType integerType;

    @BeforeEach
    void setUp() {
        fieldType = new FieldType();
        fieldType.setType("integer");
        fieldType.setMin("10");
        fieldType.setMax("100");
        integerType = new IntegerType(fieldType);
    }

    @Test
    void givenNonNumberShouldThrowValidationException() throws Exception { ;
        assertThrows(FieldValidationException.class, () -> integerType.validate("dsfa"));
    }

    @Test
    void givenSmallNumberShouldThrowValidationException() throws Exception {
        Exception ex = assertThrows(FieldValidationException.class, () -> integerType.validate("2"));
        assertEquals("Value is less than minimum value", ex.getMessage());
    }

    @Test
    void givenLargeNumberShouldThrowValidationException() throws Exception {
        Exception ex = assertThrows(FieldValidationException.class, () -> integerType.validate("102"));
        assertEquals("Value is greater than minimum value", ex.getMessage());
    }

    @Test
    void givenNumberWithinRangeValidationShouldPass() throws Exception {
        integerType.validate("80");
    }
}