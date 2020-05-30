package com.mmadu.identity.models.authorization;

import com.mmadu.identity.models.authorization.errors.AuthorizationError;
import lombok.Data;

@Data
public class AuthorizationResult {
    private RedirectData data;
    private boolean complete;
    private AuthorizationError error;
    private String redirectUri;
    private String intermediatePage;
    private String state;
}
