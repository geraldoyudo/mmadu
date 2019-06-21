package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;

public abstract class AbstractFieldTypeConverter implements FieldTypeConverter {
    private FieldType fieldType;

    public AbstractFieldTypeConverter(FieldType fieldType) {
        this.fieldType = fieldType;
    }
}
