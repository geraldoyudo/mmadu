package com.mmadu.identity.entities;

import com.mmadu.security.api.DomainPayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@Document
@EqualsAndHashCode
public class GrantAuthorization implements DomainPayload {
    @Id
    private String id;
    @NotEmpty(message = "domain id is required")
    private String domainId;
    private String userId;
    private String username;
    @NotEmpty(message = "client id is required")
    private String clientId;
    @NotEmpty(message = "client identifier is required")
    private String clientIdentifier;
    @NotEmpty(message = "client instance id is required")
    private String clientInstanceId;
    private List<String> refreshTokens = new LinkedList<>();
    private List<String> accessTokens = new LinkedList<>();
    private ZonedDateTime activationTime;
    private ZonedDateTime expiryTime;
    private ZonedDateTime issuedTime;
    private boolean revoked;
    private boolean active;
    private boolean expired;
    private ZonedDateTime revokedTime;
    private List<String> scopes;
    private String redirectUri;
    private GrantData data;
    private boolean redirectUriSpecified;
    private String grantType;
    private boolean refreshTokenIssued;
    private List<String> userAuthorities;
    private List<String> userRoles;
    private List<String> userGroups;
    private List<String> authorities;
    private String state;

    public void addRefreshToken(Token token) {
        this.refreshTokens.add(token.getId());
    }

    public void addAccessToken(Token token) {
        this.accessTokens.add(token.getId());
    }
}
