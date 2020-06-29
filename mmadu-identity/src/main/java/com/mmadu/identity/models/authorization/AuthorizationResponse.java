package com.mmadu.identity.models.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;

@Data
@EqualsAndHashCode
public class AuthorizationResponse {
    @JsonProperty("scopes")
    private List<String> scopes = Collections.emptyList();
    boolean authorize;
}
