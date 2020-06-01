package com.mmadu.identity.models;

import com.mmadu.identity.entities.ClientCredentials;

import java.util.Optional;

public interface ClientCredentialPayLoad {
    String getClientIdentifier();

    Optional<ClientCredentials> getClientCredentials();
}
