package com.mmadu.identity.models.user;

import java.util.List;

public interface MmaduUser {
    String getId();
    String getDomainId();
    String getUsername();
    List<String> getRoles();
    List<String> getAuthorities();
}
