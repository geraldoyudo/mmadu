package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

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
            BigDecimal decimal = new BigDecimal(text);
            String min = fieldType.getMin();
            if (!StringUtils.isEmpty(min) && decimal.compareTo(new BigDecimal(min)) < 0) {
                throw new FieldValidationException("Value is less than minimum value");
            }
            String max = fieldType.getMax();
            if (!StringUtils.isEmpty(max) && decimal.compareTo(new BigDecimal(max)) > 0) {
                throw new FieldValidationException("Value is greater than minimum value");
            }
        } catch (NumberFormatException ex) {
            throw new FieldValidationException(ex.getMessage());
        }
    }
}
