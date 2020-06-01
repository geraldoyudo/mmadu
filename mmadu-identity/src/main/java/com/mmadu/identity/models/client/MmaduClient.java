package com.mmadu.identity.models.client;

import com.mmadu.identity.entities.ClientCredentials;
import com.mmadu.identity.entities.ClientType;

import java.util.List;

public interface MmaduClient {
    String getDomainId();

    String getClientIdentifier();

    List<String> getAuthorities();

    List<String> getRedirectUris();

    ClientType getClientType();

    ClientCredentials getCredentials();
}
