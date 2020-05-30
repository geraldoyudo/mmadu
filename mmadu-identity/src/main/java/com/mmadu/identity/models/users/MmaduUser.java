package com.mmadu.identity.models.users;

import java.util.List;

public interface MmaduUser {
    String getId();
    String getDomainId();
    String getUsername();
    List<String> getRoles();
    List<String> getAuthorities();
}
