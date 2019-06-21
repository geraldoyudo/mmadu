package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;

import java.math.BigInteger;

@DataFieldType(name = "integer")
public class IntegerType extends AbstractFieldTypeConverter {
    public IntegerType(FieldType fieldType) {
        super(fieldType);
    }

    @Override
    public Object convertToObject(String text) throws FieldConversionException {
        return new BigInteger(text);
    }

    @Override
    public void validate(String text) throws FieldValidationException {
        try {
            new BigInteger(text);
        } catch (NumberFormatException ex) {
            throw new FieldValidationException(ex.getMessage());
        }
    }
}
