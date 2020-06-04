package com.mmadu.identity.models.authorization.errors;

public class UnsupportedResponseType extends AuthorizationError {

    @Override
    public String getError() {
        return "unsupported_response_type";
    }
}
