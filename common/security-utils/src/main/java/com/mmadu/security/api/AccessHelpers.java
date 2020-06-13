package com.mmadu.security.api;

import org.springframework.util.StringUtils;

public final class AccessHelpers {

    private AccessHelpers() {

    }

    public static String hasAnyRole(String... roles) {
        String anyRoles = StringUtils.arrayToDelimitedString(roles, ",");
        return "hasAnyRole('" + anyRoles + "')";
    }

    public static String hasRole(String role) {
        return "hasRole('" + role + "')";
    }

    public static String hasAnyAuthority(String... authorities) {
        String anyAuthorities = StringUtils.arrayToDelimitedString(authorities, ",");
        return "hasAnyAuthority('" + anyAuthorities + "')";
    }

    public static String hasAuthority(String authority) {
        return "hasAuthority('" + authority + "')";
    }
}
