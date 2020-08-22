package com.mmadu.registration.entities;

import com.mmadu.registration.models.FieldOptions;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Document
public class FieldType implements Serializable {
    @Id
    private String id;
    @Indexed(unique = true)
    @NotEmpty
    private String name;
    @NotEmpty
    private String markup;
    private String fieldTypePattern;
    private String type;
    private String enclosingElement = "div";
    private List<String> classes;
    private String style;
    private String script;
    private String css;
    private String max;
    private String min;
    private List<FieldOptions> options;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarkup() {
        return markup;
    }

    public void setMarkup(String markup) {
        this.markup = markup;
    }

    public String getFieldTypePattern() {
        return fieldTypePattern;
    }

    public void setFieldTypePattern(String fieldTypePattern) {
        this.fieldTypePattern = fieldTypePattern;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnclosingElement() {
        return enclosingElement;
    }

    public void setEnclosingElement(String enclosingElement) {
        this.enclosingElement = enclosingElement;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public List<FieldOptions> getOptions() {
        return options;
    }

    public void setOptions(List<FieldOptions> options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FieldType fieldType = (FieldType) o;

        return new EqualsBuilder()
                .append(id, fieldType.id)
                .append(name, fieldType.name)
                .append(markup, fieldType.markup)
                .append(fieldTypePattern, fieldType.fieldTypePattern)
                .append(type, fieldType.type)
                .append(enclosingElement, fieldType.enclosingElement)
                .append(classes, fieldType.classes)
                .append(style, fieldType.style)
                .append(script, fieldType.script)
                .append(css, fieldType.css)
                .append(max, fieldType.max)
                .append(min, fieldType.min)
                .append(options, fieldType.options)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(markup)
                .append(fieldTypePattern)
                .append(type)
                .append(enclosingElement)
                .append(classes)
                .append(style)
                .append(script)
                .append(css)
                .append(max)
                .append(min)
                .append(options)
                .toHashCode();
    }
}
