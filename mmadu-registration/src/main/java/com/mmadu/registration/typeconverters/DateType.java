package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@DataFieldType(name = "date")
public class DateType extends AbstractFieldTypeConverter {
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;

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
            LocalDate date = LocalDate.parse(text, dateTimeFormatter);
            String min = fieldType.getMin();
            if (!StringUtils.isEmpty(min) && date.isBefore(LocalDate.parse(min, dateTimeFormatter))) {
                throw new FieldValidationException("Value is earlier than minimum date");
            }
            String max = fieldType.getMax();
            if (!StringUtils.isEmpty(max) && date.isAfter(LocalDate.parse(max, dateTimeFormatter))) {
                throw new FieldValidationException("Value is later than maximum date");
            }
        } catch (Exception ex) {
            throw new FieldValidationException(ex.getMessage());
        }
    }
}
