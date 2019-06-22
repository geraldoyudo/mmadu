package com.mmadu.registration.providers;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.util.Map;

import static com.mmadu.registration.utils.EntityUtils.createField;
import static com.mmadu.registration.utils.EntityUtils.createFieldType;
import static org.hamcrest.CoreMatchers.equalTo;

public class ThymeleafFieldContextResolverTest {
    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    private final FieldContextResolver resolver = new ThymeleafFieldContextResolver();

    private Field field;
    private FieldType fieldType;

    @Test
    public void resolveContext() {
        fieldType = createFieldType("1");
        field = createField("1", "1");
        field.setStyle("background: #00ff");
        Map<String,Object> contextMap = resolver.resolveContext(field, fieldType);
        collector.checkThat(contextMap.get("field"), equalTo(field));
        collector.checkThat(contextMap.get("type"), equalTo(fieldType));
        collector.checkThat(contextMap.get("inputField"), equalTo("th:with=\"var_1=${'name'}\" th:field='*{properties[\"__${var_1}__\"]}'"));
        collector.checkThat(contextMap.get("inputStyle"), equalTo("th:style=\"'background: #00ff'\""));
        collector.checkThat(contextMap.get("errorStyle"), equalTo("th:classappend=\"${#fields.hasErrors('properties[name]')}? fieldError\""));
        collector.checkThat(contextMap.get("errorDisplay"), equalTo("<div class=\"errorMessage\" th:each=\"err : ${#fields.errors('properties[name]')}\" th:text=\"${err}\" ></div>"));
    }


}