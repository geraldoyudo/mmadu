package com.mmadu.identity.models.token;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public interface TokenClaim {
    @JsonProperty("iss")
    String getIssuer();

    @JsonProperty("sub")
    String getSubject();

    @JsonProperty("aud")
    String getAudience();

    @JsonProperty("exp")
    ZonedDateTime getExpirationTime();

    @JsonProperty("nbf")
    ZonedDateTime getActivationTime();

    @JsonProperty("iat")
    ZonedDateTime getIssueTime();

    @JsonProperty("jti")
    String getTokenIdentifier();

    @JsonProperty("client_id")
    String getClientIdentifier();

}
