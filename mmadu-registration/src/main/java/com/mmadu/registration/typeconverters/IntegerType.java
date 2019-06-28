package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;
import org.springframework.util.StringUtils;

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
            BigInteger integer = new BigInteger(text);
            String min = fieldType.getMin();
            if (!StringUtils.isEmpty(min) && integer.compareTo(new BigInteger(min)) < 0) {
                throw new FieldValidationException("Value is less than minimum value");
            }
            String max = fieldType.getMax();
            if (!StringUtils.isEmpty(max) && integer.compareTo(new BigInteger(max)) > 0) {
                throw new FieldValidationException("Value is greater than minimum value");
            }
        } catch (NumberFormatException ex) {
            throw new FieldValidationException(ex.getMessage());
        }
    }
}
