package com.mmadu.identity.providers.client.instance;

public interface CredentialDataHashMatcher {

    boolean matches(String data, String hashedData);
}
