package com.mmadu.registration.entities;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@Document
@EqualsAndHashCode
@Builder
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
}
