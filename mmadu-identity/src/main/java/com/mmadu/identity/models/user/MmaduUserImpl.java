package com.mmadu.identity.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MmaduUserImpl implements MmaduUser {
    @JsonProperty("id")
    private String id;
    @JsonProperty("domainId")
    private String domainId;
    @JsonProperty("username")
    private String username;
    @JsonProperty("authorities")
    private List<String> authorities;
    @JsonProperty("roles")
    private List<String> roles;
    @JsonProperty("groups")
    private List<String> groups;
}
