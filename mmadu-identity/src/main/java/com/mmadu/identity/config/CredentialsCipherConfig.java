package com.mmadu.identity.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.security.*;
import java.security.cert.Certificate;

@Configuration
public class CredentialsCipherConfig {
    @Value("${mmadu.identity.credentials.key-store:classpath:keys/credentials.jks}")
    private Resource keyStoreResource;
    @Value("${mmadu.identity.credentials.store-password:password}")
    private String storePassword;
    @Value("${mmadu.identity.credentials.key-password:password}")
    private String keyPassword;
    @Value("${mmadu.identity.credentials.key-alias:mmadu}")
    private String keyAlias;

    @Bean
    @Qualifier("credentials")
    public KeyPair credentialsCipherKeyPair() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(keyStoreResource.getInputStream(), storePassword.toCharArray());
        Key key = keyStore.getKey(keyAlias, keyPassword.toCharArray());
        if (key instanceof PrivateKey) {
            Certificate cert = keyStore.getCertificate(keyAlias);
            PublicKey publicKey = cert.getPublicKey();
            return new KeyPair(publicKey, (PrivateKey) key);
        }
        throw new IllegalStateException("invalid key in keystore");
    }
}
