package com.mmadu.identity.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@EqualsAndHashCode
public class DomainConfiguration implements HasDomain {
    @Id
    private String id;
    @Indexed(unique = true)
    private String domainId;
}
