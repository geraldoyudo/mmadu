package com.mmadu.identity.models.token;

import com.mmadu.identity.security.clients.ClientSecretCredentialRequestPayload;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class TokenIntrospectionRequest implements ClientSecretCredentialRequestPayload {
    @NotEmpty(message = "token is required")
    private String token;
    private String token_type_hint = "";
    private String client_id;
    private String client_secret;

    @Override
    public String getClientIdentifier() {
        return client_id;
    }

    @Override
    public String getClientSecret() {
        return client_secret;
    }
}
