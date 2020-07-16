package com.mmadu.registration.services;

import com.mmadu.registration.config.RestResourceConfig;
import com.mmadu.registration.config.SerializationConfig;
import com.mmadu.registration.utils.WiremockExtension;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.mmadu.registration.utils.EntityUtils.DOMAIN_ID;

@ExtendWith({
        SpringExtension.class,
        WiremockExtension.class
})
@SpringBootTest(classes = {
        MmaduServiceClientImpl.class,
        SerializationConfig.class,
        RestResourceConfig.class
})
@TestPropertySource(properties = {
        "mmadu.userService.url=http://localhost:19998",
        "mmadu.domainKey=12345",
        "spring.jackson.serialization.WRITE_DATES_AS_TIMESTAMPS=false",
        "spring.security.oauth2.client.provider.mmadu.token-uri=http://localhost:19998/clients/token"
})
@EnableAutoConfiguration
@ImportAutoConfiguration(OAuth2ClientAutoConfiguration.class)
public class MmaduServiceClientImplTest {
    public static final String AUTHORIZATION_HEADER = "Bearer eyJraWQiOiIxMjMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI1ZWUzNzhhZDQ3NDg5MTI5Y2M0OWIzYjAiLCJyb2xlcyI6W10sImlzcyI6Im1tYWR1LmNvbSIsImdyb3VwcyI6WyJ0ZXN0Iiwic2FtcGxlIl0sImF1dGhvcml0aWVzIjpbXSwiY2xpZW50X2lkIjoiMjJlNjViNzItOTIzNC00MjgxLTlkNzMtMzIzMDA4OWQ0OWE3IiwiZG9tYWluX2lkIjoiMCIsImF1ZCI6InRlc3QiLCJuYmYiOjE1OTE5NjU4OTIsInVzZXJfaWQiOiIxMTExMTExMTEiLCJzY29wZSI6InZpZXcgZWRpdCIsImV4cCI6Mjk5MTk2NjE5MiwiaWF0IjoxNTkxOTY1ODkyLCJqdGkiOiJmNWJmNzVhNi0wNGEwLTQyZjctYTFlMC01ODNlMjljZGU4NmMifQ.EgjaDmX03BYWbBdnFx4rYLTlT3wnRqnILd-pLWZLrUPZ48llyuzPoB2dJ3QcNSuzxb9koOS55513nzpKekOAkcDuA3XP7OTxw_4X5rar7xQiA3gEnQ1RAgUcUCOXGmlzl5f9XQsdHtY-WxMuh-qgdELqH8fkb4p0HcAHOOdhKOivSoIGu1uGBrbmT8RFUcAti1mmUzDJM0RFn0JZc7IULizoaibEh-mGNuBn0AN2ZhK1xRM-tbKIOZBp5_wVY1YcGc7M1bO-VeCmg2dWilZC9_9GT2X2t4E1vXoz1a4OkiBZx27GhZwJCSWnrve5OwRPf4ONTV7B0FZqJxFP3yQTIg";

    @Value("classpath:responses/client-credentials-token.json")
    private Resource tokenFile;

    @Autowired
    private MmaduUserServiceClient mmaduUserServiceClient;

    @Test
    public void addUsers() throws Exception {
        stubTokenApi();
        stubFor(post(urlPathEqualTo("/domains/" + DOMAIN_ID + "/users"))
                .withRequestBody(matchingJsonPath(
                        "$.username", equalTo("user"))
                )
                .withRequestBody(matchingJsonPath(
                        "$.password", equalTo("password"))
                )
                .withRequestBody(matchingJsonPath(
                        "$.date", equalTo("1993-01-01")
                ))
                .withHeader(HttpHeaders.AUTHORIZATION, equalTo(AUTHORIZATION_HEADER))
                .willReturn(aResponse()
                        .withStatus(204)));
        Map<String, Object> userProperties = new HashMap<>();
        userProperties.put("username", "user");
        userProperties.put("password", "password");
        userProperties.put("date", LocalDate.of(1993, 1, 1));
        mmaduUserServiceClient.addUsers(DOMAIN_ID, userProperties);

        verify(postRequestedFor(urlMatching("/domains/" + DOMAIN_ID + "/users"))
                .withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(matchingJsonPath(
                        "$.username", equalTo("user"))
                )
                .withRequestBody(matchingJsonPath(
                        "$.password", equalTo("password"))
                )
                .withRequestBody(matchingJsonPath(
                        "$.date", equalTo("1993-01-01")
                ))
        );
    }

    private void stubTokenApi() throws Exception {
        stubFor(post(urlPathEqualTo("/clients/token"))
                .withRequestBody(equalTo("grant_type=client_credentials&scope=a.*.**+r.*.**"))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo("application/x-www-form-urlencoded;charset=UTF-8"))
                .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Basic NWY2NWQwY2QtNjEwMy00ODM4LTk3NjYtOTEzMjU4OWU3ZTg5OjExMzIzMjMyMw=="))
                .withHeader(HttpHeaders.ACCEPT, equalTo("application/json;charset=UTF-8"))
                .withHeader(HttpHeaders.HOST, equalTo("localhost:19998"))
                .withHeader(HttpHeaders.CONNECTION, equalTo("keep-alive"))
                .withHeader(HttpHeaders.CONTENT_LENGTH, equalTo("49"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                        .withBody(IOUtils.toString(tokenFile.getInputStream(), StandardCharsets.UTF_8))
                ));
    }
}