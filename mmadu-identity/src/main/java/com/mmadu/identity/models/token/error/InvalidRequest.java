package com.mmadu.identity.models.token.error;

public class InvalidRequest extends TokenError {
    public InvalidRequest() {
        super();
    }
    public InvalidRequest(String description, String uri){
        super(description, uri);
    }

    @Override
    public String getError() {
        return "invalid_request";
    }
}
