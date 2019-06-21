package com.mmadu.registration.typeconverters;

public interface FieldTypeConverter {
    Object convertToObject(String text) throws FieldConversionException;

    void validate(String text) throws FieldValidationException;
}
