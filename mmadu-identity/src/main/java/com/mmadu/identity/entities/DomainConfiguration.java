package com.mmadu.identity.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class DomainConfiguration {
    @Id
    private String id;
    @Indexed(unique = true)
    private String domainId;
}
