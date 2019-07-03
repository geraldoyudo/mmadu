package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class FieldConverterFactory {
    private FieldTypeConverterResolver resolver;

    public FieldConverterFactory() throws Exception {
        this.resolver = FieldTypeConverterResolver.getInstance();
    }

    public FieldTypeConverter getConverterForType(FieldType type) {
        Class<? extends AbstractFieldTypeConverter> converterClass =
                resolver.getConverterForType(type.getType())
                        .orElseThrow(() -> new IllegalArgumentException("Field type not supported"));
        try {
            return converterClass.getDeclaredConstructor(FieldType.class).newInstance(type);
        } catch (NoSuchMethodException | InstantiationException |
                InvocationTargetException | IllegalAccessException ex) {
            throw new IllegalArgumentException("class type not supported. ensure class has the correct signature");
        }
    }
}
