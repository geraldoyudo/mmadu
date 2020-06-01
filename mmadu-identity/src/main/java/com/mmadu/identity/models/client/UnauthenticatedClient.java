package com.mmadu.identity.models.client;

import com.mmadu.identity.entities.ClientCredentials;
import com.mmadu.identity.entities.ClientType;

import java.util.List;

public class UnauthenticatedClient implements MmaduClient {
    @Override
    public String getDomainId() {
        return null;
    }

    @Override
    public String getClientIdentifier() {
        return null;
    }

    @Override
    public List<String> getAuthorities() {
        return null;
    }

    @Override
    public List<String> getRedirectUris() {
        return null;
    }

    @Override
    public ClientType getClientType() {
        return null;
    }

    @Override
    public ClientCredentials getCredentials() {
        return null;
    }
}
