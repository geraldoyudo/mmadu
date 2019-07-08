package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;
import org.springframework.util.StringUtils;

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
            Object dateTime = parseDateTime(text);
            if (dateTime instanceof ZonedDateTime) {
                validateZonedDateTime((ZonedDateTime) dateTime);
            } else {
                validateLocalDateTime((LocalDateTime) dateTime);
            }
        } catch (Exception ex) {
            throw new FieldValidationException(ex.getMessage());
        }
    }

    private void validateZonedDateTime(ZonedDateTime dateTime) throws FieldValidationException {
        ZonedDateTime zonedDateTime = dateTime;
        String min = fieldType.getMin();
        if (!StringUtils.isEmpty(min) && zonedDateTime.isBefore(ZonedDateTime.parse(min, dateTimeFormatter))) {
            throw new FieldValidationException("Value is earlier than minimum datetime");
        }
        String max = fieldType.getMax();
        if (!StringUtils.isEmpty(max) && zonedDateTime.isAfter(ZonedDateTime.parse(max, dateTimeFormatter))) {
            throw new FieldValidationException("Value is later than maximum datetime");
        }
    }

    private void validateLocalDateTime(LocalDateTime dateTime) throws FieldValidationException {
        LocalDateTime localDateTime = dateTime;
        String min = fieldType.getMin();
        if (!StringUtils.isEmpty(min) && localDateTime.isBefore(LocalDateTime.parse(min, dateTimeFormatter))) {
            throw new FieldValidationException("Value is earlier than minimum datetime");
        }
        String max = fieldType.getMax();
        if (!StringUtils.isEmpty(max) && localDateTime.isAfter(LocalDateTime.parse(max, dateTimeFormatter))) {
            throw new FieldValidationException("Value is later than maximum datetime");
        }
    }
}
