package com.mmadu.identity.models.token.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedClient extends TokenError {
    public UnauthorizedClient() {
        super();
    }
    public UnauthorizedClient(String description, String uri){
        super(description, uri);
    }

    @Override
    public String getError() {
        return "unauthorized_client";
    }
}
