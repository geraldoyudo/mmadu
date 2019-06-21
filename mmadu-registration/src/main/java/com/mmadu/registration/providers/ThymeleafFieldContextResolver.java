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
                "th:class=\"${#fields.hasErrors('properties[var_%s]')}? fieldError\"", field.getId()));
        context.put("errorDisplay", String.format("<p class=\"errorMessage\">" +
                "<ul>" +
                    "<li th:each=\"err : ${#fields.errors('properties[var_%s]')}\" th:text=\"${err}\" />" +
                "</ul>" +
                "</p>", field.getId()));
        return context;
    }
}
