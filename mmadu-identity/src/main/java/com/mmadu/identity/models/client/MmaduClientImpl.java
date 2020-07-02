package com.mmadu.identity.models.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mmadu.identity.entities.Client;
import com.mmadu.identity.entities.ClientCredentials;
import com.mmadu.identity.entities.ClientInstance;
import com.mmadu.identity.entities.ClientType;

import java.util.List;

public class MmaduClientImpl implements MmaduClient {
    private Client client;
    private ClientInstance clientInstance;

    public MmaduClientImpl(Client client, ClientInstance clientInstance) {
        this.client = client;
        this.clientInstance = clientInstance;
    }

    @Override
    public String getDomainId() {
        return client.getDomainId();
    }

    @Override
    public String getClientIdentifier() {
        return clientInstance.getIdentifier();
    }

    @Override
    public List<String> getAuthorities() {
        return clientInstance.getAuthorities();
    }

    @Override
    public List<String> getRedirectUris() {
        return clientInstance.getRedirectionUris();
    }

    @Override
    public ClientType getClientType() {
        return clientInstance.getClientType();
    }

    @Override
    @JsonIgnore
    public ClientCredentials getCredentials() {
        return clientInstance.getCredentials();
    }

    @Override
    public boolean issueRefreshTokens() {
        return clientInstance.isIssueRefreshTokens();
    }

    @Override
    public Long getAccessTokenTTLSeconds() {
        return clientInstance.getAccessTokenTTLSeconds();
    }

    @Override
    public Long getRefreshTokenTTLSeconds() {
        return clientInstance.getRefreshTokenTTLSeconds();
    }

    @Override
    public List<String> getResources() {
        return clientInstance.getResources();
    }

    @Override
    public boolean isIncludeUserAuthorities() {
        return clientInstance.isIncludeUserAuthorities();
    }

    @Override
    public boolean isIncludeUserRoles() {
        return clientInstance.isIncludeUserRoles();
    }

    @Override
    public boolean isIncludeUserGroups() {
        return clientInstance.isIncludeUserGroups();
    }

    @Override
    public List<String> getScopes() {
        return clientInstance.getScopes();
    }

    @Override
    public String getTokenCategory() {
        return clientInstance.getTokenCategory();
    }

    @Override
    public Long getImplicitGrantTypeTTLSeconds() {
        return clientInstance.getImplicitGrantTypeTTLSeconds();
    }

    @Override
    public String getInstanceId() {
        return clientInstance.getId();
    }

    @Override
    public String getId() {
        return client.getId();
    }

    @Override
    public Long getAuthorizationCodeGrantTypeTTLSeconds() {
        return clientInstance.getAuthorizationCodeGrantTypeTTLSeconds();
    }

    @Override
    public List<String> getSupportedGrantTypes() {
        return clientInstance.getSupportedGrantTypes();
    }
}
