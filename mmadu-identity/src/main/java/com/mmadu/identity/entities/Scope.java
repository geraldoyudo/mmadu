package com.mmadu.identity.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@EqualsAndHashCode
@CompoundIndexes({
        @CompoundIndex(name = "domain-code", def = "{'domainId': 1, 'code': 1}", unique = true)
}
)
public class Scope {
    @Id
    private String id;
    private String domainId;
    private String code;
    private String name;
    private String description;
}
