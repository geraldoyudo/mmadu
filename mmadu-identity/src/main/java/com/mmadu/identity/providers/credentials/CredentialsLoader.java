package com.mmadu.identity.providers.credentials;

import com.mmadu.identity.exceptions.CredentialFormatException;

public interface CredentialsLoader<T> {

    T loadCredentialById(String credentialId) throws CredentialFormatException;
}
