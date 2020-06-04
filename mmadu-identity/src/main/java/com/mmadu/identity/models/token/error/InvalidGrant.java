package com.mmadu.identity.models.token.error;

public class InvalidGrant extends TokenError {
    public InvalidGrant() {
        super();
    }
    public InvalidGrant(String description, String uri){
        super(description, uri);
    }

    @Override
    public String getError() {
        return "invalid_grant";
    }
}
