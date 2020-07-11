package com.mmadu.identity.models.authorization;

import lombok.Data;

@Data
public class AuthorizationProfile {
    private boolean autoApproveScopes = false;
}
