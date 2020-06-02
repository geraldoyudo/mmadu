package com.mmadu.identity.models.token;

import com.mmadu.identity.entities.GrantAuthorization;
import lombok.Builder;
import lombok.Getter;

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
}
