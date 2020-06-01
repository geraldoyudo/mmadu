package com.mmadu.identity.models.client;

import java.util.List;

public interface MmaduClient {
    String getDomainId();

    String getClientIdentifier();

    List<String> getAuthorities();

    List<String> getRedirectUris();
}
