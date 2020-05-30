package com.mmadu.identity.models.authorization;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode
public class AuthorizationRequest {
    @NotEmpty(message = "response_type is required")
    private String response_type;
    @NotEmpty(message = "client_id is required")
    private String client_id;
    private String redirect_uri;
    private String scope;
    private String state;
}
