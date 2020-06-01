package com.mmadu.identity.models.token;

import com.mmadu.identity.entities.Client;
import com.mmadu.identity.entities.ClientInstance;
import com.mmadu.identity.entities.GrantAuthorization;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class TokenSpecification {
    private String type;
    private Object claim;
    private String domainId;
    private GrantAuthorization grantAuthorization;
    private Client client;
    private ClientInstance clientInstance;
    private Map<String, Object> configuration;
    private String userId;
    private List<String> scopes;
    private List<String> labels;
}
