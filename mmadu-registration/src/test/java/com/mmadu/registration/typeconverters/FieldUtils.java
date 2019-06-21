package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;

import static com.mmadu.registration.utils.EntityUtils.createFieldType;

public final class FieldUtils {
    private FieldUtils() {
    }

    public static FieldType fieldTypeWithPattern(String pattern) {
        FieldType field = createFieldType("1");
        field.setFieldTypePattern(pattern);
        return field;
    }
}
