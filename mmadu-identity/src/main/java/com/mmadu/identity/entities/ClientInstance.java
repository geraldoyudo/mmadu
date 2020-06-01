package com.mmadu.identity.entities;

import com.mmadu.identity.utils.ClientProfileUtils;
import com.mmadu.identity.utils.GrantTypeUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@Document
@EqualsAndHashCode
public class ClientInstance implements HasDomain {
    @Id
    private String id;
    private String clientId;
    private ClientType clientType = ClientType.CONFIDENTIAL;
    @NotEmpty(message = "client profile cannot be empty")
    private String clientProfile = ClientProfileUtils.WEB_APP;
    @Valid
    private ClientCredentials credentials;
    private String identifier = UUID.randomUUID().toString();
    private List<String> redirectionUris = Collections.emptyList();
    private List<String> allowedHosts = Collections.emptyList();
    private boolean tlsEnabled = true;
    private List<String> supportedGrantTypes = Collections.singletonList(GrantTypeUtils.AUTHORIZATION_CODE);
    @NotEmpty(message = "domainId cannot be empty")
    private String domainId;
    @NotNull(message = "resources is required")
    @Size(min = 1, message = "at least one resource is required")
    private List<String> resources = Collections.emptyList();
    private List<String> authorities = Collections.emptyList();
    private boolean issueRefreshTokens = true;
}
