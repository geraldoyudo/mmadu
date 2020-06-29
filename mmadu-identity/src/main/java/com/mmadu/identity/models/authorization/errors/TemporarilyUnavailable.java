package com.mmadu.identity.models.authorization.errors;

public class TemporarilyUnavailable extends AuthorizationError {

    @Override
    public String getError() {
        return "temporarily_unavailable";
    }
}
