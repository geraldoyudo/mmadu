package com.mmadu.identity.models.token.error;

public class UnsupportedGrantType extends TokenError {
    public UnsupportedGrantType() {
        super();
    }
    public UnsupportedGrantType(String description, String uri){
        super(description, uri);
    }

    @Override
    public String getError() {
        return "unsupported_grant_type";
    }
}
