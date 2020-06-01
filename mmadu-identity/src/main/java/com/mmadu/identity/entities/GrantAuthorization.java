package com.mmadu.identity.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Data
@Document
@EqualsAndHashCode
public class GrantAuthorization {
    @Id
    private String id;
    @NotEmpty(message = "domain id is required")
    private String domainId;
    private String userId;
    @NotEmpty(message = "client id is required")
    private String clientId;
    @NotEmpty(message = "client instance id is required")
    private String clientInstanceId;
    private List<String> refreshTokens = Collections.emptyList();
    private List<String> accessTokens = Collections.emptyList();
    private ZonedDateTime activationTime;
    private ZonedDateTime expiryTime;
    private boolean revoked;
    private boolean active;
    private boolean expired;
    private ZonedDateTime revokedTime;
    private List<String> scopes;
    private String redirectUri;
    private GrantData data;
    private boolean redirectUriSpecified;
}
