package com.mmadu.registration.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Data
@Document
@EqualsAndHashCode
public class FieldType {
    @Id
    private String id;
    @Indexed(unique = true)
    @NotEmpty
    private String name;
    @NotEmpty
    private String markup;
    private String fieldTypePattern;
    private String type;
}
