package com.mmadu.registration.providers;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;

import java.util.Map;

public interface FieldContextResolver {

    Map<String, Object> resolveContext(Field field, FieldType type);
}
