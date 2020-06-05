package com.mmadu.identity.models.token;

import com.mmadu.identity.entities.GrantAuthorization;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class TokenSpecification {
    private String provider;
    private String domainId;
    private GrantAuthorization grantAuthorization;
    private Map<String, Object> configuration;
    private List<String> scopes;
    private List<String> labels;
    private String type;
    private ZonedDateTime expirationTime;
    private ZonedDateTime issueTime;
    private ZonedDateTime activationTime;
    private List<String> authorities;
    @Builder.Default
    private boolean active = true;
}
