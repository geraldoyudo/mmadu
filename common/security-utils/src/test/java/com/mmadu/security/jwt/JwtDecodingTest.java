package com.mmadu.security.jwt;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

public class JwtDecodingTest {
    private JwtDecoder decoder;
    private static final String PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100a31b559ff90788f92436bb61ad0d528cf088a9190a97c7dbf98539409663d54d3fb4a089ccd77ca49165d8d5a76b21b30fc733a558569647498b182dc4a06ea7fd1022b761877c9776d0db5107b8f3e0c67ba0f101315be5989d0a33c6a431a3de07c071457672c6266a1e89d079222c42031ca7c3b563a913c41eee45d20ddaf58a92014adbc4bbe135c055c604380b649b3178540fc4a0c2f7c46aa90f62422d5ae621332bacf6771f2319ae03936fdaf346abdc599e3b63cae4c0d4d8ec5832f1c61b5e370005c3aed880130513970b79de6d5734aa11dcf8fb866ea9d0cfafd55d0c4f27a13763f1d193ca402c5c8a65e198a7500b3e9928552e19a40d3d0203010001";

    @Test
    void testDecode() throws Exception {
        X509EncodedKeySpec pubKeySpec
                = new X509EncodedKeySpec(Hex.decodeHex(PUBLIC_KEY));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
        decoder = NimbusJwtDecoder.withPublicKey((RSAPublicKey) pubKey)
                .signatureAlgorithm(SignatureAlgorithm.RS256)
                .build();
        Jwt jwt = decoder.decode("eyJraWQiOiIxMjMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI1ZWUyNDUxMTgxOTIwZDdlNjU0MThkODEiLCJyb2xlcyI6W10sImlzcyI6Im1tYWR1LmNvbSIsImdyb3VwcyI6WyJ0ZXN0Iiwic2FtcGxlIl0sImF1dGhvcml0aWVzIjpbXSwiY2xpZW50X2lkIjoiMjJlNjViNzItOTIzNC00MjgxLTlkNzMtMzIzMDA4OWQ0OWE3IiwiZG9tYWluX2lkIjoiMCIsImF1ZCI6InRlc3QiLCJuYmYiOjE1OTE4ODkyOTEsInVzZXJfaWQiOiIxMTExMTExMTEiLCJzY29wZSI6InZpZXcgZWRpdCIsImV4cCI6MTU5MTg4OTU5MSwiaWF0IjoxNTkxODg5MjkxLCJqdGkiOiI0NjM2MzZkNS1kMDdkLTQxNzItODQyOC02NzVmMzU5MGZlYmIifQ.VfG_sB6_mFBXHiQKGBBecKq4gCnDl7MAsHXRAyfcsc4DFt9nZfPjbMl28GmhtdRzLX7WQDaF2xpRPd4eKdnN1Nil8tsO_AgWFyWVYsbDfgXDbxAzYFTQE5iB3671GcHEzxcx7_bOjiM1UWSdSr13LS3TS6STIfq822u0l8rKB__Q7UbSJ30chEKDwST6eWwxO1mpmL_HNBpAPD9RBkh4C4N6zkTP0FecsyKnAnvJ7Or073xvG5KIt5hEdV0hEi4pqstKJ59vOojh9QMFD2ObTTwjBE4J0Kdd4E5eMJb7kgo3U8CUusJicE4UNfKYfEou-u_cDnzRYx1_4JpMqU7qcg");
        System.out.println(jwt.getClaims());
    }

}
