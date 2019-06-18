package com.mmadu.registration.providers;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringWriter;

@Component
public class VelocityFieldMarkupGenerator implements FieldMarkupGenerator {
    private FieldContextResolver fieldContextResolver;
    private VelocityEngine velocityEngine;

    @Autowired
    public void setFieldContextResolver(FieldContextResolver fieldContextResolver) {
        this.fieldContextResolver = fieldContextResolver;
    }

    @Autowired
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Override
    public String resolveField(Field field, FieldType type) {
        StringWriter writer = new StringWriter();
        velocityEngine.evaluate(new VelocityContext(fieldContextResolver.resolveContext(field, type)), writer,
                "", type.getMarkup()
        );
        return writer.getBuffer().toString();
    }
}
