package com.mmadu.identity.providers.token.claims;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.identity.config.TokenCreationConfig;
import com.mmadu.identity.services.client.MmaduClientService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@SpringBootTest(classes = {
        TokenCreationConfig.class,
        UserGrantAccessTokenClaimStrategy.class

})
class UserGrantAccessTokenClaimStrategyTest {

    @Value("classpath:claims/authentication-code-grant-access-token-claims.json")
    private Resource resource;

    @Autowired
    @Qualifier("jwt")
    private ObjectMapper objectMapper;

    @MockBean
    private MmaduClientService mmaduClientService;

    @Test
    void claimSerializationTest() throws Exception {
        ZonedDateTime testTime = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
        var claims = UserGrantAccessTokenClaimStrategy.UserAccessTokenClaim.builder()
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
        JSONAssert.assertEquals(IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8),
                objectMapper.writeValueAsString(claims), true);
    }
}