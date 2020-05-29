package com.mmadu.identity.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mmadu.identity.utils.ClientProfileUtils;
import com.mmadu.identity.utils.GrantTypeUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;

@Data
@Document
@EqualsAndHashCode
public class ClientInstance implements HasDomain {
    @Id
    private String id;
    private String clientId;
    private ClientType clientType = ClientType.CONFIDENTIAL;
    private String clientProfile = ClientProfileUtils.WEB_APP;
    private ClientCredentials credentials;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String identifier;
    private List<String> redirectionUris = Collections.emptyList();
    private List<String> allowedHosts = Collections.emptyList();
    private boolean tlsEnabled = true;
    private List<String> supportedGrantTypes = Collections.singletonList(GrantTypeUtils.AUTHORIZATION_CODE);
    @NotEmpty(message = "domainId cannot be empty")
    private String domainId;
}
