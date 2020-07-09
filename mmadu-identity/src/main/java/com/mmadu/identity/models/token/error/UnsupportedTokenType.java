package com.mmadu.identity.models.token.error;

public class UnsupportedTokenType extends TokenError {
    public UnsupportedTokenType() {
        super();
    }
    public UnsupportedTokenType(String description, String uri){
        super(description, uri);
    }

    @Override
    public String getError() {
        return "unsupported_token_type";
    }
}
