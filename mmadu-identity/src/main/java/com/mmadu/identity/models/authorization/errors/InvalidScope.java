package com.mmadu.identity.models.authorization.errors;

public class InvalidScope extends AuthorizationError {

    @Override
    public String getError() {
        return "invalid_scope";
    }
}
