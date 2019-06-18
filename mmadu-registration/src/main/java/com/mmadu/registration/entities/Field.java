package com.mmadu.registration.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Document
@CompoundIndexes({
        @CompoundIndex(name = "domain_name", def = "{'domainId': 1, 'name': 1}", unique = true),
        @CompoundIndex(name = "domain_property", def = "{'domainId': 1, 'property': 1}", unique = true)
})
@EqualsAndHashCode
public class Field {
    private String id;
    @NotEmpty
    private String domainId;
    @NotEmpty
    private String name;
    @NotEmpty
    private String placeholder;
    @NotEmpty
    private String property;
    @NotEmpty
    private String fieldTypeId;
    @NotNull
    private String style;
    @NotEmpty
    private String label;
    private int order;
}
