package com.mmadu.identity.entities.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = JwtTokenCredentials.class, name = "jwt")
})
public interface TokenCredentials {

    String getType();

    @JsonIgnore
    String getTokenString();

    @JsonIgnore
    String getTokenIdentifier();
}
