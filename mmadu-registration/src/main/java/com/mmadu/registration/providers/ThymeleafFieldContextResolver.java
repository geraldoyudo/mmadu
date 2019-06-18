package com.mmadu.registration.providers;

import com.mmadu.registration.entities.Field;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ThymeleafFieldContextResolver implements FieldContextResolver {
    @Override
    public Map<String, Object> resolveContext(Field field) {
        Map<String, Object> context = new HashMap<>();
        context.put("field", field);
        context.put("inputField", String.format("th:field=\"*%s\"", field.getProperty()));
        context.put("inputStyle", String.format("th:style=\"%s\"", field.getStyle()));
        return context;
    }
}
