package com.mmadu.registration.providers;

import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.utils.VelocityEngineConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.mmadu.registration.utils.EntityUtils.createField;
import static com.mmadu.registration.utils.EntityUtils.createFieldType;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        VelocityFieldMarkupGenerator.class,
        ThymeleafFieldContextResolver.class,
        VelocityEngineConfig.class
})
public class VelocityFieldMarkupGeneratorTest {
    @Autowired
    private FieldMarkupGenerator fieldMarkupGenerator;

    @Test
    void testMarkupField() {
        String markup = fieldMarkupGenerator.resolveField(createField("1", ","), createFieldType("1"));
        assertThat(markup, equalTo("<div><p> Name: <input type='text' th:with=\"var_1=${'name'}\" " +
                "th:field='*{properties[\"__${var_1}__\"]}' th:style=\"'background: 00ff'\"/> /></div>"));

    }

    @Test
    void givenClassesAndStylesWhenResolveFieldShouldAddAppropriately() {
        FieldType type = createFieldType("1");
        type.setClasses(asList("one", "two"));
        type.setStyle("color: red");
        type.setCss("#inputButton {color:red}");
        String markup = fieldMarkupGenerator.resolveField(createField("1", ","), type);
        assertThat(markup, equalTo("<div class='one two' style='color: red'>" +
                "<style>#inputButton {color:red}</style>" +
                "<p> Name: <input type='text' th:with=\"var_1=${'name'}\" " +
                "th:field='*{properties[\"__${var_1}__\"]}' th:style=\"'background: 00ff'\"/> /></div>"));

    }
}