package com.mmadu.registration.entities;

import com.mmadu.security.api.DomainPayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@EqualsAndHashCode
@Document
public class RegistrationProfile implements DomainPayload {
    @Id
    private String id;
    @Indexed(unique = true)
    private String domainId;
    private String defaultRedirectUrl;
    private List<String> defaultRoles;
    private List<String> defaultAuthorities;
    private String headerOne;
    private String headerTwo;
    private String headerThree;
    private String instruction;
    private String submitButtonTitle;
}
