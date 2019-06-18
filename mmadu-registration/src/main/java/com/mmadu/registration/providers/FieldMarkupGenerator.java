package com.mmadu.registration.providers;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;

public interface FieldMarkupGenerator {

    String resolveField(Field field, FieldType type);
}
