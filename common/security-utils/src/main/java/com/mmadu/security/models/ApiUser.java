package com.mmadu.security.models;

import java.util.List;

public interface ApiUser {
    String getDomainId();

    String getClientId();

    List<String> getAuthorities();

    List<String> getRoles();
}
