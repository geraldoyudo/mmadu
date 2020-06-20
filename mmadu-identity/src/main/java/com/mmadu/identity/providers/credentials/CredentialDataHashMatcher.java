package com.mmadu.identity.providers.credentials;

public interface CredentialDataHashMatcher {

    boolean matches(String data, String hashedData);
}
