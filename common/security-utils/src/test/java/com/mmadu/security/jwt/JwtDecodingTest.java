package com.mmadu.security.jwt;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

public class JwtDecodingTest {
    private JwtDecoder decoder;


    @Test
    void testDecode() throws Exception {
        RSAKey key = RSAKey.parse(IOUtils.readInputStreamToString(getClass().getResourceAsStream("/keys/jwt/key-data.json")));
        decoder = NimbusJwtDecoder.withPublicKey(key.toRSAPublicKey())
                .signatureAlgorithm(SignatureAlgorithm.RS256)
                .build();
        Jwt jwt = decoder.decode("eyJraWQiOiIxMjMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI1ZWRiYmIzOTJjZTMyMjRiNjkxN2M4NGEiLCJyb2xlcyI6WyJhZG1pbiIsInN0ZXdhcmQiXSwiaXNzIjoibW1hZHUuY29tIiwiZ3JvdXBzIjpbInRlc3QiLCJzYW1wbGUiXSwiYXV0aG9yaXRpZXMiOlsibWFuYWdlLXVzZXJzIl0sImNsaWVudF9pZCI6IjIyZTY1YjcyLTkyMzQtNDI4MS05ZDczLTMyMzAwODlkNDlhNyIsImRvbWFpbl9pZCI6IjAiLCJhdWQiOiJ0ZXN0IiwibmJmIjoiMTU5MTQ1OTE3NCIsInVzZXJfaWQiOiIxMTExMTExMTEiLCJzY29wZSI6InZpZXcgZWRpdCIsImV4cCI6IjE1OTE0NTk0NzQiLCJpYXQiOiIxNTkxNDU5MTc0IiwianRpIjoiNTFjZDIyZWMtZWE3Zi00ZTFiLWE2YjMtMDlhYjhhMTlkOTlkIn0.XICK-3lb8oX9BcKezzJZEsK9B_k8vSEfLggXb1gkaMtxpYe4MpneL-a4ymEbVKMJrki92Yl0rbiAqIwEXz8WKNOWg6J6QAN3iQrQGRxgMA5-c-0Pa2uDAq-HX3V1oNk615EvDFfzVuLQW6rhsi4AtECD06B0Cx-nnEMQKDN7VFyvnGENXCZXUeACj4odLpoDz5i_IqahQ8DwPAH043f5UwIMROYLAm2SKzyHxo3YUMQARk-JVHeDHKoxB12Qh-9fQsK9YfwvjfZFASLifedEV72xA35hj0QJu92Z7_Kr9effaMXP_xdMFCzI5_6zFq08ImoWk-1wYMaRY4ojX4tycA");
        System.out.println(jwt.getClaims());
    }

}
