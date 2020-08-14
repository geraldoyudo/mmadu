package com.mmadu.identity.models.authorization;

import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.entities.Token;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mmadu.identity.utils.MapUtils.putIfNotEmpty;
import static com.mmadu.identity.utils.MapUtils.putMandatory;

@Data
@Builder
public class ImplicitRedirectData implements RedirectData {
    private String accessToken;
    private String tokenType;
    private ZonedDateTime expiryTimestamp;
    private List<String> scopes;
    private String state;

    @Override
    public Map<String, List<String>> toParams() {
        Map<String, List<String>> params = new HashMap<>();
        putMandatory("access_token", accessToken, params);
        putMandatory("token_type", tokenType, params);
        putMandatory("expires_in", getTimeStampString(), params);
        putIfNotEmpty("scope", getScopesString(), params);
        putMandatory("state", state, params);
        return params;
    }

    private String getTimeStampString() {
        return Optional.ofNullable(expiryTimestamp)
                .map(t -> ZonedDateTime.now().until(t, ChronoUnit.SECONDS))
                .map(Object::toString)
                .orElse("");
    }

    private String getScopesString() {
        if (scopes == null) {
            return "";
        } else {
            return String.join(" ", scopes);
        }
    }

    public static ImplicitRedirectData fromTokenAndAuthorization(Token token, GrantAuthorization authorization) {
        return ImplicitRedirectData.builder()
                .accessToken(token.getTokenString())
                .expiryTimestamp(token.getExpiryTime())
                .scopes(token.getScopes())
                .state(authorization.getState())
                .tokenType(token.getCategory())
                .build();
    }
}
