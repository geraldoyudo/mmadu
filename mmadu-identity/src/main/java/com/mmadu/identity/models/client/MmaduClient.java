package com.mmadu.identity.models.client;

import com.mmadu.identity.entities.ClientCredentials;
import com.mmadu.identity.entities.ClientType;
import com.mmadu.identity.utils.TokenCategoryUtils;

import java.util.Collections;
import java.util.List;

public interface MmaduClient {
    String getDomainId();

    String getClientIdentifier();

    List<String> getAuthorities();

    List<String> getRedirectUris();

    ClientType getClientType();

    ClientCredentials getCredentials();

    default boolean issueRefreshTokens() {
        return false;
    }

    default List<String> getResources() {
        return Collections.emptyList();
    }

    default Long getAccessTokenTTLSeconds() {
        return 0L;
    }

    default Long getRefreshTokenTTLSeconds() {
        return 0L;
    }

    default boolean isIncludeUserRoles() {
        return false;
    }

    default boolean isIncludeUserAuthorities() {
        return false;
    }

    default boolean isIncludeUserGroups() {
        return false;
    }

    default List<String> getScopes() {
        return Collections.emptyList();
    }

    default String getTokenCategory() {
        return TokenCategoryUtils.CATEGORY_BEARER;
    }

    default Long getImplicitGrantTypeTTLSeconds() {
        return 60 * 60L;
    }

    default String getInstanceId() {
        return "";
    }

    default String getId() {
        return "";
    }
}
