package com.mmadu.identity.models.authorization.errors;

public class ServerError extends AuthorizationError {

    @Override
    public String getError() {
        return "server_error";
    }
}
