package com.mmadu.registration.providers;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ThymeleafFieldContextResolver implements FieldContextResolver {
    @Override
    public Map<String, Object> resolveContext(Field field, FieldType type) {
        Map<String, Object> context = new HashMap<>();
        context.put("field", field);
        context.put("type", type);
        context.put("inputField", String.format("th:with=\"var_%s=${'%s'}\" th:field='*{properties[\"__${var_%s}__\"]}'",
                field.getId(), field.getProperty(), field.getId()));
        context.put("inputStyle", String.format("th:style=\"'%s'\"",
                Optional.ofNullable(field.getStyle()).orElse("")));
        context.put("errorStyle", String.format(
                "th:classappend=\"${#fields.hasErrors('properties[%s]')}? fieldError\"", field.getProperty()));
        context.put("errorDisplay", String.format(
                    "<div class=\"errorMessage\" th:each=\"err : ${#fields.errors('properties[%s]')}\" th:text=\"${err}\" ></div>"
                , field.getProperty()));
        return context;
    }
}
