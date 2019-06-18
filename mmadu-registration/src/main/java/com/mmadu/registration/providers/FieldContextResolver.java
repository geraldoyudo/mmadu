package com.mmadu.registration.providers;

import com.mmadu.registration.entities.Field;

import java.util.Map;

public interface FieldContextResolver {

    Map<String, Object> resolveContext(Field field);
}
