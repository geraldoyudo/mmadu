package com.mmadu.identity.models.token.error;

public class InvalidScope extends TokenError {
    public InvalidScope() {
        super();
    }
    public InvalidScope(String description, String uri){
        super(description, uri);
    }

    @Override
    public String getError() {
        return "invalid_scope";
    }
}
