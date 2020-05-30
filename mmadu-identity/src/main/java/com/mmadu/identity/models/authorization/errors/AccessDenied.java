package com.mmadu.identity.models.authorization.errors;

public class AccessDenied extends AuthorizationError {

    @Override
    public String getError() {
        return "access_denied";
    }
}
