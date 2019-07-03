package com.mmadu.registration.services;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mmadu.registration.config.SerializationConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.mmadu.registration.utils.EntityUtils.DOMAIN_ID;

@RunWith(SpringRunner.class)
@Import({
        MmaduServiceClientImpl.class,
        SerializationConfig.class
})
@TestPropertySource(properties = {
        "mmadu.userService.url=http://localhost:19999",
        "mmadu.domainKey=12345",
        "spring.jackson.serialization.WRITE_DATES_AS_TIMESTAMPS=false"
})
public class MmaduServiceClientImplTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(19999);
    @Rule
    public ErrorCollector collector = new ErrorCollector();

    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";

    @Value("${mmadu.domainKey}")
    private String domainKey;

    @Autowired
    private MmaduUserServiceClient mmaduUserServiceClient;

    @Test
    public void addUsers() {
        stubFor(post(urlPathEqualTo("/domains/" + DOMAIN_ID + "/users"))
                .withRequestBody(matchingJsonPath(
                        "$.username", WireMock.equalTo("user"))
                )
                .withRequestBody(matchingJsonPath(
                        "$.password", WireMock.equalTo("password"))
                )
                .withRequestBody(matchingJsonPath(
                        "$.date", WireMock.equalTo("1993-01-01")
                ))
                .withHeader("domain-auth-token", WireMock.equalTo(domainKey))
                .willReturn(aResponse()
                        .withStatus(204)));
        Map<String, Object> userProperties = new HashMap<>();
        userProperties.put("username", "user");
        userProperties.put("password", "password");
        userProperties.put("date", LocalDate.of(1993,1,1));
        mmaduUserServiceClient.addUsers(DOMAIN_ID, userProperties);

        verify(postRequestedFor(urlMatching("/domains/" + DOMAIN_ID + "/users"))
                .withHeader("domain-auth-token", equalTo(domainKey))
                .withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(matchingJsonPath(
                        "$.username", WireMock.equalTo("user"))
                )
                .withRequestBody(matchingJsonPath(
                        "$.password", WireMock.equalTo("password"))
                )
                .withRequestBody(matchingJsonPath(
                        "$.date", WireMock.equalTo("1993-01-01")
                ))
        );
    }
}