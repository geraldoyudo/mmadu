package com.mmadu.registration.providers;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.mmadu.registration.utils.EntityUtils.createField;
import static com.mmadu.registration.utils.EntityUtils.createFieldType;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ThymeleafFieldContextResolverTest {
    private final FieldContextResolver resolver = new ThymeleafFieldContextResolver();

    private Field field;
    private FieldType fieldType;

    @Test
    void resolveContext() {
        fieldType = createFieldType("1");
        field = createField("1", "1");
        field.setStyle("background: #00ff");
        fieldType.setMax("10");
        fieldType.setMin("0");
        Map<String, Object> contextMap = resolver.resolveContext(field, fieldType);

        assertAll(
                () -> assertThat(contextMap.get("field"), equalTo(field)),
                () -> assertThat(contextMap.get("type"), equalTo(fieldType)),
                () -> assertThat(contextMap.get("inputField"),
                        equalTo("th:with=\"var_1=${'name'}\" th:field='*{properties[\"__${var_1}__\"]}'")),
                () -> assertThat(contextMap.get("inputStyle"),
                        equalTo("th:style=\"'background: #00ff'\"")),
                () -> assertThat(contextMap.get("errorStyle"),
                        equalTo("th:classappend=\"${#fields.hasErrors('properties[name]')}? fieldError\"")),
                () -> assertThat(contextMap.get("errorDisplay"),
                        equalTo("<div class=\"errorMessage\" th:each=\"err : ${#fields.errors('properties[name]')}\" " +
                                "th:text=\"${err}\" ></div>")),
                () -> assertThat(contextMap.get("minValue"), equalTo("min='0'")),
                () -> assertThat(contextMap.get("maxValue"), equalTo("max='10'"))
        );
    }


}