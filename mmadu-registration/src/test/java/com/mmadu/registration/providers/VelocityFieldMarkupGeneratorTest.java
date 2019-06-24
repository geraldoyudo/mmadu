package com.mmadu.registration.providers;

import com.mmadu.registration.utils.VelocityEngineConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.mmadu.registration.utils.EntityUtils.createField;
import static com.mmadu.registration.utils.EntityUtils.createFieldType;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        VelocityFieldMarkupGenerator.class,
        ThymeleafFieldContextResolver.class,
        VelocityEngineConfig.class
})
public class VelocityFieldMarkupGeneratorTest {
    @Autowired
    private FieldMarkupGenerator fieldMarkupGenerator;

    @Test
    public void testMarkupField() {
        String markup = fieldMarkupGenerator.resolveField(createField("1", ","), createFieldType("1"));
        assertThat(markup, equalTo("<p> Name: <input type='text' th:with=\"var_1=${'name'}\" " +
                "th:field='*{properties[\"__${var_1}__\"]}' th:style=\"'background: 00ff'\"/> />"));

    }
}