package com.mmadu.registration.services;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
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

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.mmadu.registration.utils.EntityUtils.DOMAIN_ID;

@RunWith(SpringRunner.class)
@Import(MmaduServiceClientImpl.class)
@TestPropertySource(properties = {
        "mmadu.userService.url=http://localhost:19999",
        "mmadu.domainKey=12345"
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
                .withHeader("domain-auth-token", WireMock.equalTo(domainKey))
                .willReturn(aResponse()
                        .withStatus(204)));
        Map<String, Object> userProperties = new HashMap<>();
        userProperties.put("username", "user");
        userProperties.put("password", "password");

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
        );
    }
}