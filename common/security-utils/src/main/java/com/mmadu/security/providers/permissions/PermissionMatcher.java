package com.mmadu.security.providers.permissions;

public interface PermissionMatcher {

    boolean matchesPermission(String grantedPermission, String accessingPermission);
}
