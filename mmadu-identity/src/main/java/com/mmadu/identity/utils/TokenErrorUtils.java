package com.mmadu.identity.utils;

import com.mmadu.identity.exceptions.TokenException;
import com.mmadu.identity.models.token.error.InvalidClient;
import com.mmadu.identity.models.token.error.InvalidRequest;

public final class TokenErrorUtils {

    private TokenErrorUtils() {

    }

    public static TokenException invalidCodeError() {
        return new TokenException(new InvalidRequest("code.invalid", ""));
    }

    public static TokenException redirectUriIsRequired() {
        return new TokenException(new InvalidRequest("redirect_uri.required", ""));
    }

    public static TokenException invalidGrantData() {
        return new TokenException(new InvalidRequest("grant_data.invalid", ""));
    }

    public static TokenException invalidClient() {
        return new TokenException(new InvalidClient("client.domain.invalid", ""));
    }

    public static TokenException invalidRequest() {
        return new TokenException(new InvalidRequest("request.invalid", ""));
    }
}
