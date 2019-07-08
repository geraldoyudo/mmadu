package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@DataFieldType(name = "text")
public class TextType extends AbstractFieldTypeConverter {
    private String pattern;

    public TextType(FieldType fieldType) {
        super(fieldType);
        pattern = fieldType.getFieldTypePattern();
    }

    @Override
    public Object convertToObject(String text) throws FieldConversionException {
        return text;
    }

    @Override
    public void validate(String text) throws FieldValidationException {
        if (!StringUtils.isEmpty(pattern) && !Pattern.matches(pattern, text)) {
            throw new FieldValidationException("Text does not match the required pattern: " + pattern);
        }

    }
}
