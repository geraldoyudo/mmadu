package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

import java.lang.reflect.InvocationTargetException;

@Component
public class FieldValidatorFactory {
    private FieldTypeConverterResolver resolver;

    public FieldValidatorFactory() throws Exception {
        this.resolver = FieldTypeConverterResolver.getInstance();
    }

    public Validator getValidatorForFieldType(FieldType type, Field field) {
        Class<? extends AbstractFieldTypeConverter> converterClass =
                resolver.getConverterForType(type.getType())
                        .orElseThrow(() -> new IllegalArgumentException("Field type not supported"));
        try {
            return new FieldTypeValidator(converterClass.getDeclaredConstructor(FieldType.class).newInstance(type),
                    field);
        } catch (NoSuchMethodException | InstantiationException |
                InvocationTargetException | IllegalAccessException ex) {
            throw new IllegalArgumentException("class type not supported. ensure class has the correct signature");
        }
    }
}
