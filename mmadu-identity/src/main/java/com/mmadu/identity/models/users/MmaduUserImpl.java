package com.mmadu.identity.models.users;

import lombok.Data;

import java.util.List;

@Data
public class MmaduUserImpl implements MmaduUser {
    private String id;
    private String domainId;
    private String username;
    private List<String> authorities;
    private List<String> roles;
}
