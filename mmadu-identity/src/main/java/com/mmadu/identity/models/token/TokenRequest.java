package com.mmadu.identity.models.token;

import com.mmadu.identity.security.clients.ClientSecretCredentialRequestPayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minidev.json.annotate.JsonIgnore;

@Data
@EqualsAndHashCode
public class TokenRequest implements ClientSecretCredentialRequestPayload {
    private String grant_type;
    private String code;
    private String redirect_uri;
    private String client_id;
    private String client_secret;
    private String refresh_token;
    private String scope;
    private String username;
    private String password;

    @Override
    @JsonIgnore
    public String getClientIdentifier() {
        return client_id;
    }

    @Override
    @JsonIgnore
    public String getClientSecret() {
        return client_secret;
    }
}
