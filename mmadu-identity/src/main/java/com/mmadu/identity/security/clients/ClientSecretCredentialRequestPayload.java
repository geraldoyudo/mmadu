package com.mmadu.identity.security.clients;

public interface ClientSecretCredentialRequestPayload extends ClientCredentialRequestPayload {

    String getClientSecret();
}
