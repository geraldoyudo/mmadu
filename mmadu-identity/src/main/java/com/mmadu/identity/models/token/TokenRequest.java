package com.mmadu.identity.models.token;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class TokenRequest {
    private String grant_type;
    private String code;
    private String redirect_uri;
    private String client_id;
    private String client_secret;
    private String refresh_token;
    private String scope;
    private String username;
    private String password;
}
