package com.mmadu.registration.utils;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.entities.RegistrationProfile;

public final class EntityUtils {
    public static final String DOMAIN_ID = "domain-1";
    public static final String DOMAIN_CODE = "1234";

    private EntityUtils() {
    }

    public static FieldType createFieldType(String id) {
        FieldType fieldType = new FieldType();
        fieldType.setId(id);
        fieldType.setName("text" + id);
        fieldType.setMarkup("<p> $field.label: <input type='text' $inputField $inputStyle/> />");
        fieldType.setFieldTypePattern("");
        fieldType.setType("text");
        return fieldType;
    }

    public static Field createField(String id, String fieldTypeId) {
        Field field = new Field();
        field.setDomainId(DOMAIN_ID);
        field.setId(id);
        field.setName("Name" + id);
        field.setPlaceholder("Name");
        field.setProperty("name");
        field.setFieldTypeId(fieldTypeId);
        field.setLabel("Name");
        field.setStyle("background: 00ff");
        return field;
    }

    public static Field createField(String id, String name, String fieldTypeId) {
        Field field = new Field();
        field.setDomainId(DOMAIN_ID);
        field.setId(id);
        field.setName(name);
        field.setPlaceholder(name);
        field.setProperty(name);
        field.setFieldTypeId(fieldTypeId);
        return field;
    }

    public static Field createField(String id, String name, String property, String fieldTypeId) {
        Field field = new Field();
        field.setDomainId(DOMAIN_ID);
        field.setId(id);
        field.setName(name);
        field.setPlaceholder(name);
        field.setProperty(property);
        field.setFieldTypeId(fieldTypeId);
        field.setCode(name);
        return field;
    }

    public static RegistrationProfile createRegistrationProfile(String id) {
        RegistrationProfile profile = new RegistrationProfile();
        profile.setDomainId(DOMAIN_ID);
        profile.setCode(DOMAIN_CODE);
        profile.setDefaultRedirectUrl("https://google.com");
        profile.setId(id);
        return profile;
    }

    public static RegistrationProfile createRegistrationProfile(String id, String domainId) {
        RegistrationProfile profile = new RegistrationProfile();
        profile.setDomainId(domainId);
        profile.setCode(DOMAIN_CODE);
        profile.setDefaultRedirectUrl("https://google.com");
        profile.setId(id);
        return profile;
    }
}
