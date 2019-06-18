package com.mmadu.registration.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@CompoundIndexes({
        @CompoundIndex(name = "domain_name", def = "{'domainId': 1, 'name': 1}", unique = true),
        @CompoundIndex(name = "domain_property", def = "{'domainId': 1, 'property': 1}", unique = true)
})
public class Field {
    private String id;
    private String domainId;
    private String name;
    private String placeholder;
    private String property;
    private String fieldTypeId;
}
