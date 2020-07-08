package com.mmadu.identity.security.clients;

import com.mmadu.identity.models.client.MmaduClient;

import javax.servlet.http.HttpServletRequest;

public interface ClientCredentialRequestAuthenticationStrategy {
    boolean apply(HttpServletRequest request, ClientCredentialRequestPayload payload, MmaduClient client);

    boolean isPermitted(HttpServletRequest request, ClientCredentialRequestPayload payload, MmaduClient client);
}
