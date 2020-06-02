package com.mmadu.identity.providers.token.claims;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.identity.config.TokenCreationConfig;
import com.mmadu.identity.services.client.MmaduClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {
        TokenCreationConfig.class,
        AuthorizationCodeGrantAccessTokenClaimStrategy.class

})
class AuthorizationCodeGrantAccessTokenClaimStrategyTest {
    @Autowired
    @Qualifier("jwt")
    private ObjectMapper objectMapper;

    @MockBean
    private MmaduClientService mmaduClientService;

    @Test
    void claimSerializationTest() throws Exception {
        ZonedDateTime testTime = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
        var claims = AuthorizationCodeGrantAccessTokenClaimStrategy.AuthorizationCodeAccessTokenClaim.builder()
                .activationTime(testTime)
                .clientIdentifier("122333")
                .audience(List.of("service-1", "service-2"))
                .domainId("2")
                .expirationTime(testTime.plusDays(1))
                .issuer("test")
                .issueTime(testTime.minusMinutes(20))
                .scope("view edit")
                .subject("subject")
                .tokenIdentifier("3849283")
                .userId("2333")
                .build();
        assertEquals("{\"scope\":\"view edit\",\"iss\":\"test\",\"sub\":\"subject\",\"aud\":[\"service-1\",\"service-2\"],\"exp\":\"1577923200\",\"nbf\":\"1577836800\",\"iat\":\"1577835600\",\"jti\":\"3849283\",\"client_id\":\"122333\",\"domain_id\":\"2\",\"user_id\":\"2333\"}",
                objectMapper.writeValueAsString(claims));
    }
}