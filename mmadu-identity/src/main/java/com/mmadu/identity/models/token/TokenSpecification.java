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
    private final String provider;
    private final String domainId;
    private final GrantAuthorization grantAuthorization;
    private final Map<String, Object> configuration;
    private final List<String> scopes;
    private final List<String> labels;
    private final String type;
    private final ZonedDateTime expirationTime;
    private final ZonedDateTime issueTime;
    private final ZonedDateTime activationTime;
    private final List<String> authorities;
    private final String category;
    @Builder.Default
    private final boolean active = true;
}
