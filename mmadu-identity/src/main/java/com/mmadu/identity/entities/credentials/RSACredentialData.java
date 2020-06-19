package com.mmadu.identity.entities.credentials;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mmadu.identity.exceptions.CredentialFormatException;
import com.mmadu.identity.providers.credentials.CredentialDataHashMatcher;
import com.mmadu.identity.providers.credentials.CredentialDataHashProvider;
import com.mmadu.identity.providers.credentials.CredentialDecryptionProvider;
import com.mmadu.identity.providers.credentials.CredentialEncryptionProvider;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.text.ParseException;

@Data
public class RSACredentialData implements CredentialData, HasVerificationKey {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String keyData;

    @Override
    public String getType() {
        return "rsa";
    }

    @Override
    @JsonIgnore
    public byte[] getVerificationKey() {
        try {
            return RSAKey.parse(keyData).toPublicKey().getEncoded();
        } catch (ParseException | JOSEException ex) {
            throw new CredentialFormatException("public key cannot be parsed", ex);
        }
    }

    @Override
    public boolean matches(Object credential, CredentialDataHashMatcher matcher) {
        return false;
    }

    @Override
    public void hashData(CredentialDataHashProvider provider) {
        // do nothing
    }

    @Override
    public void encryptData(CredentialEncryptionProvider provider) {
        keyData = provider.encrypt(keyData);
    }

    @Override
    public void decryptData(CredentialDecryptionProvider provider) {
        keyData = provider.decrypt(keyData);
    }
}
