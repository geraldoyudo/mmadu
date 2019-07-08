package com.mmadu.registration.typeconverters;

import com.mmadu.registration.entities.FieldType;

public abstract class AbstractFieldTypeConverter implements FieldTypeConverter {
    protected FieldType fieldType;

    public AbstractFieldTypeConverter(FieldType fieldType) {
        this.fieldType = fieldType;
    }
}
