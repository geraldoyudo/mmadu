package com.mmadu.identity.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Data
@Document
@EqualsAndHashCode
public class Client implements HasDomain {
    @Id
    private String id;
    @NotEmpty(message = "domain id cannot be empty")
    private String domainId;
    @NotEmpty(message = "client name cannot be empty")
    private String name;
    @NotEmpty(message = "client code cannot be empty")
    private String code = UUID.randomUUID().toString();
    @NotEmpty(message = "client application url cannot be empty")
    private String applicationUrl;
    private String logoUrl;
}
