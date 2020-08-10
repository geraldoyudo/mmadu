package com.mmadu.notifications.service.utils;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.IOUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.core.io.ClassPathResource;

import java.time.ZonedDateTime;

public final class TokenGeneratorUtils {
    private static TokenGeneratorUtils instance;

    private RSAKey rsaJWK;
    private JWSSigner jwsSigner;
    private JWTClaimsSet claimSetTemplate;

    private TokenGeneratorUtils() {

    }

    private void initialize() throws Exception {
        rsaJWK = RSAKey.parse(IOUtils.readInputStreamToString(
                new ClassPathResource("/keys/key-data.json").getInputStream()
        ));

        jwsSigner = new RSASSASigner(rsaJWK);
        claimSetTemplate = JWTClaimsSet.parse(
                IOUtils.readInputStreamToString(
                        new ClassPathResource("/claims/lasting-token-claim.json").getInputStream()
                )
        );
    }

    public String generateTokenWithAuthorities(String... authorities) throws Exception {
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder(claimSetTemplate);
        builder.claim("exp", ZonedDateTime.now().plusSeconds(5L).toEpochSecond());
        builder.claim("nbf", ZonedDateTime.now().toEpochSecond());
        builder.claim("iat", ZonedDateTime.now().toEpochSecond());
        builder.claim("scope", String.join(" ", authorities));
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
                builder.build());

        signedJWT.sign(jwsSigner);
        return signedJWT.serialize();
    }

    public static synchronized TokenGeneratorUtils getInstance() throws Exception {
        if (instance == null) {
            instance = new TokenGeneratorUtils();
            instance.initialize();
        }
        return instance;
    }

}
