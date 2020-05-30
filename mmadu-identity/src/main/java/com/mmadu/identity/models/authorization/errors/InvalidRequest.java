package com.mmadu.identity.models.authorization.errors;

public class InvalidRequest extends AuthorizationError {

    @Override
    public String getError() {
        return "invalid_request";
    }
}
