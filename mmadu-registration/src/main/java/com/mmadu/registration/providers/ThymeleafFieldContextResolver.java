package com.mmadu.registration.providers;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ThymeleafFieldContextResolver implements FieldContextResolver {

    public static final String INPUT_FIELD_TEMPLATE = "th:with=\"var_%s=${'%s'}\" " +
            "th:field='*{properties[\"__${var_%s}__\"]}'";
    public static final String INPUT_STYLE_TEMPLATE = "th:style=\"'%s'\"";
    public static final String ERROR_STYLE_TEMPLATE = "th:classappend=\"${#fields.hasErrors('properties[%s]')}? " +
            "fieldError\"";
    public static final String ERROR_DISPLAY_TEMPLATE = "<div class=\"errorMessage\" " +
            "th:each=\"err : ${#fields.errors('properties[%s]')}\" th:text=\"${err}\" ></div>";

    @Override
    public Map<String, Object> resolveContext(Field field, FieldType type) {
        Map<String, Object> context = new HashMap<>();
        context.put("field", field);
        context.put("type", type);
        context.put("inputField", String.format(INPUT_FIELD_TEMPLATE,
                field.getId(), field.getProperty(), field.getId()));
        context.put("inputStyle", String.format(INPUT_STYLE_TEMPLATE,
                Optional.ofNullable(field.getStyle()).orElse("")));
        context.put("errorStyle", String.format(
                ERROR_STYLE_TEMPLATE, field.getProperty()));
        context.put("errorDisplay", String.format(
                ERROR_DISPLAY_TEMPLATE
                , field.getProperty()));
        context.put("required", field.isRequired() ? "required" : "");
        context.put("maxValue", StringUtils.isEmpty(type.getMax()) ? "" :
                String.format("max='%s'", type.getMax()));
        context.put("minValue", StringUtils.isEmpty(type.getMin()) ? "" :
                String.format("min='%s'", type.getMin()));
        return context;
    }
}
