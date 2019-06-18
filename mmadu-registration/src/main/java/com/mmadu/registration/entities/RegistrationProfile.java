package com.mmadu.registration.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode
@Document
public class RegistrationProfile {
    @Id
    private String id;
    @Indexed(unique = true)
    private String domainId;
    private String defaultRedirectUrl;
}
