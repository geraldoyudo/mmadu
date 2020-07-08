package com.mmadu.identity.models.token;

import javax.validation.constraints.NotEmpty;

public class TokenIntrospectionRequest {
    @NotEmpty(message = "token is required")
    private String token;
    private String token_type_hint = "";
}
