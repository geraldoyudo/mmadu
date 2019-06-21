package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@DataFieldType(name = "date")
public class DateType extends AbstractFieldTypeConverter {
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public DateType(FieldType fieldType) {
        super(fieldType);
        Optional.ofNullable(fieldType.getFieldTypePattern())
                .ifPresent(pattern -> {
                    dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
                });
    }

    @Override
    public Object convertToObject(String text) throws FieldConversionException {
        try {
            return LocalDate.parse(text, dateTimeFormatter);
        } catch (DateTimeParseException ex) {
            throw new FieldConversionException(ex.getMessage());
        }
    }

    @Override
    public void validate(String text) throws FieldValidationException {
        try {
            LocalDate.parse(text, dateTimeFormatter);
        } catch (Exception ex) {
            throw new FieldValidationException(ex.getMessage());
        }
    }
}
