package com.mmadu.identity.models.authorization.errors;

public class UnauthorizedClient extends AuthorizationError {

    @Override
    public String getError() {
        return "unauthorized_client";
    }
}
