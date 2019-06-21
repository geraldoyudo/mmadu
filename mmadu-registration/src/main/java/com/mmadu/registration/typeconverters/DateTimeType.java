package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@DataFieldType(name = "datetime")
public class DateTimeType extends AbstractFieldTypeConverter {
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;

    public DateTimeType(FieldType fieldType) {
        super(fieldType);
        Optional.ofNullable(fieldType.getFieldTypePattern())
                .ifPresent(pattern -> {
                    dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
                });
    }

    @Override
    public Object convertToObject(String text) throws FieldConversionException {
        try {
            return parseDateTime(text);
        } catch (Exception ex) {
            throw new FieldConversionException(ex.getMessage());
        }
    }

    private Object parseDateTime(String text) throws Exception {
        try {
            return ZonedDateTime.parse(text, dateTimeFormatter);
        } catch (Exception ex) {
            return LocalDateTime.parse(text, dateTimeFormatter);
        }
    }

    @Override
    public void validate(String text) throws FieldValidationException {
        try {
            parseDateTime(text);
        } catch (Exception ex) {
            throw new FieldValidationException(ex.getMessage());
        }
    }
}
