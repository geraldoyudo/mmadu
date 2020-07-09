package com.mmadu.identity.models.token;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.entities.Token;
import com.mmadu.identity.utils.StringListUtils;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TokenIntrospectionResponse {
    @JsonProperty("active")
    private boolean active;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("client_id")
    private String clientIdentifier;
    @JsonProperty("username")
    private String username;
    @JsonProperty("token_type")
    private String tokenCategory;
    @JsonProperty("exp")
    private Long expiryTime;
    @JsonProperty("iat")
    private Long issuedTime;
    @JsonProperty("nbf")
    private Long activationTime;
    @JsonProperty("sub")
    private String subject;
    @JsonProperty("aud")
    private List<String> audience;
    @JsonProperty("iss")
    private String issuer;
    @JsonProperty("jti")
    private String tokenIdentifier;
    @JsonProperty("authorities")
    private List<String> authorities;
    @JsonProperty("user_roles")
    private List<String> userRoles;
    @JsonProperty("user_authorities")
    private List<String> userAuthorities;
    @JsonProperty("user_groups")
    private List<String> userGroups;
    @JsonProperty("user_id")
    private String userId;

    public static TokenIntrospectionResponse fromTokenAndAuthorization(Token token,
                                                                       Optional<GrantAuthorization> authorization) {
        return TokenIntrospectionResponse.builder()
                .active(true)
                .activationTime(token.getActivationTime().toEpochSecond())
                .audience(token.getAudience())
                .authorities(authorization.map(GrantAuthorization::getAuthorities).orElse(null))
                .clientIdentifier(authorization.map(GrantAuthorization::getClientIdentifier).orElse(null))
                .expiryTime(token.getExpiryTime().toEpochSecond())
                .issuedTime(authorization.map(GrantAuthorization::getIssuedTime)
                        .map(ZonedDateTime::toEpochSecond).orElse(null))
                .issuer(token.getIssuer())
                .scope(StringListUtils.toString(token.getScopes()))
                .subject(authorization.map(GrantAuthorization::getId).orElse(token.getUserId()))
                .tokenCategory(token.getCategory())
                .tokenIdentifier(token.getTokenIdentifier())
                .userAuthorities(authorization.map(GrantAuthorization::getUserAuthorities).orElse(null))
                .userGroups(authorization.map(GrantAuthorization::getUserGroups).orElse(null))
                .userRoles(authorization.map(GrantAuthorization::getUserRoles).orElse(null))
                .username(token.getUsername())
                .userId(token.getUserId())
                .build();
    }

    public static TokenIntrospectionResponse inactiveToken() {
        return TokenIntrospectionResponse.builder()
                .active(false)
                .build();
    }
}
