package com.mmadu.security.providers.permissions;

import org.springframework.util.AntPathMatcher;

public class AntStylePermissionMatcher implements PermissionMatcher {
    private AntPathMatcher matcher = new AntPathMatcher(".");

    @Override
    public boolean matchesPermission(String grantedPermission, String accessingPermission) {
        return matcher.match(grantedPermission, accessingPermission);
    }
}
