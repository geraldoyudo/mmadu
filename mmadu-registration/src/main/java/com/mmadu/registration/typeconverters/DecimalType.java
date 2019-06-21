package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;

import java.math.BigDecimal;
import java.math.BigInteger;

@DataFieldType(name = "decimal")
public class DecimalType extends AbstractFieldTypeConverter {

    public DecimalType(FieldType fieldType) {
        super(fieldType);
    }

    @Override
    public Object convertToObject(String text) throws FieldConversionException {
        return new BigDecimal(text);
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
