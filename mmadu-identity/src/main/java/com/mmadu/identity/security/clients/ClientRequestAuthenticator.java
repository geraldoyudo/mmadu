package com.mmadu.identity.security.clients;

import com.mmadu.identity.models.client.MmaduClient;

public interface ClientRequestAuthenticator {

    void authenticateClient(ClientCredentialRequestPayload request, MmaduClient client);

}
