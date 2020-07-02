package com.mmadu.identity.utils;

import com.mmadu.identity.models.authorization.AuthorizationContext;
import com.mmadu.identity.models.user.MmaduUser;

public final class AuthorizationUtils {

    private AuthorizationUtils() {

    }

    public static void ensureMmaduUserAuthorizer(AuthorizationContext context) {
        if (!(context.getAuthorizer() instanceof MmaduUser)) {
            throw new IllegalStateException("invalid authorization");
        }
    }
}
