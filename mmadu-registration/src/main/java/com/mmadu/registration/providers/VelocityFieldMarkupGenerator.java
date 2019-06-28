package com.mmadu.registration.providers;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        String enclosingTag = "<%s%s%s>%s%s</%s>";
        velocityEngine.evaluate(new VelocityContext(fieldContextResolver.resolveContext(field, type)), writer,
                "", type.getMarkup()
        );
        String content = writer.getBuffer().toString();
        String enclosingElement = type.getEnclosingElement();
        if (StringUtils.isEmpty(enclosingElement)) {
            enclosingElement = "div";
        }
        String classes = convertToString(Optional.ofNullable(type.getClasses()).orElse(Collections.emptyList()));
        String style = type.getStyle();
        String css = type.getCss();
        return String.format(enclosingTag,
                enclosingElement,
                StringUtils.isEmpty(classes)? "": String.format(" class='%s'", classes),
                StringUtils.isEmpty(style)? "": String.format(" style='%s'", style),
                StringUtils.isEmpty(css)? "": String.format("<style>%s</style>", css),
                content,
                enclosingElement
                );
    }

    private String convertToString(List<String> stringList) {
        if (stringList.isEmpty()) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();
            for (String item : stringList) {
                builder.append(item).append(" ");
            }
            return builder.toString().trim();
        }
    }
}
