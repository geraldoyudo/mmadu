package com.mmadu.identity.models.token.error;

public class InvalidClient extends TokenError {
    public InvalidClient() {
        super();
    }
    public InvalidClient(String description, String uri){
        super(description, uri);
    }

    @Override
    public String getError() {
        return "invalid_client";
    }
}
