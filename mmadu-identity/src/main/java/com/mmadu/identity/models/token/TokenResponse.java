package com.mmadu.identity.models.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonIgnore
    private ZonedDateTime expiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("jti")
    private String tokenIdentifier;

    @JsonProperty("expires_in")
    public Long getExpiresIn() {
        if (expiresIn == null) {
            return null;
        } else {
            return expiresIn.toEpochSecond();
        }
    }
}
