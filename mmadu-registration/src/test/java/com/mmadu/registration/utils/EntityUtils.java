package com.mmadu.registration.utils;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;

public final class EntityUtils {
    public static final String DOMAIN_ID = "domain-1";

    private EntityUtils() {
    }

    public static FieldType createFieldType(String id) {
        return new FieldType(id, "text" + id, "<input type='text'/>");
    }

    public static  Field createField(String id, String fieldTypeId) {
        Field field = new Field();
        field.setDomainId(DOMAIN_ID);
        field.setId(id);
        field.setName("Name" + id);
        field.setPlaceholder("Name");
        field.setProperty("name");
        field.setFieldTypeId(fieldTypeId);
        return field;
    }
    public static  Field createField(String id, String name, String fieldTypeId) {
        Field field = new Field();
        field.setDomainId(DOMAIN_ID);
        field.setId(id);
        field.setName(name);
        field.setPlaceholder(name);
        field.setProperty(name);
        field.setFieldTypeId(fieldTypeId);
        return field;
    }

    public static  Field createField(String id, String name, String property,  String fieldTypeId) {
        Field field = new Field();
        field.setDomainId(DOMAIN_ID);
        field.setId(id);
        field.setName(name);
        field.setPlaceholder(name);
        field.setProperty(property);
        field.setFieldTypeId(fieldTypeId);
        return field;
    }
}
