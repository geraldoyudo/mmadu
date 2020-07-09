package com.mmadu.registration.entities;

import com.mmadu.security.api.DomainPayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@EqualsAndHashCode
@Document
@CompoundIndexes({
        @CompoundIndex(name = "domain_code", def = "{'domainId': 1, 'code': 1}", unique = true)
})
public class RegistrationProfile implements DomainPayload {
    @Id
    private String id;
    @NotEmpty(message = "code is required")
    private String code;
    @Indexed(unique = true)
    private String domainId;
    private String defaultRedirectUrl;
    private List<String> defaultRoles;
    private List<String> defaultAuthorities;
    private List<String> defaultGroups;
    private String headerOne;
    private String headerTwo;
    private String headerThree;
    private String instruction;
    private String submitButtonTitle;
}
