package com.mmadu.identity.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@EqualsAndHashCode
public class Resource implements HasDomain {
    @Id
    private String id;
    private String domainId;
    private String identifier;
    private String name;
    private String description;
}
