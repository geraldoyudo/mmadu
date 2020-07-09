package com.mmadu.registration.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Data
@Document
public class DomainFlowConfiguration {
    @Id
    private String id;
    @NotEmpty(message = "domainId is required")
    private String domainId;
    @NotEmpty(message = "default registration code is required")
    private String defaultRegistrationCode;
}
