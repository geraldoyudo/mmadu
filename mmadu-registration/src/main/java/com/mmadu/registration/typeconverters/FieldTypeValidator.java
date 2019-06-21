package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.models.UserForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class FieldTypeValidator implements Validator {
    private AbstractFieldTypeConverter fieldTypeConverter;
    private Field field;

    public FieldTypeValidator(AbstractFieldTypeConverter fieldTypeConverter, Field field) {
        this.fieldTypeConverter = fieldTypeConverter;
        this.field = field;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserForm.class.equals(aClass);
    }

    @Override
    public void validate(Object value, Errors e) {
        String fieldPropertyName = String.format("properties['%s']", field.getProperty());

        if (field.isRequired()) {
            ValidationUtils.rejectIfEmpty(e, fieldPropertyName,
                    field.getProperty() + ".empty", new Object[]{},
                    "field is required");
        }

        UserForm form = (UserForm) value;
        form.get(field.getProperty()).ifPresent(stringValue -> {
            try {
                fieldTypeConverter.validate(stringValue);
            } catch (FieldValidationException ex) {
                e.rejectValue(fieldPropertyName, field.getProperty() + ".invalid",
                        new Object[]{}, "invalid field");
            }
        });
    }
}
