package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.endpoint.models.NotificationUser;
import com.mmadu.notifications.service.config.RestResourceConfig;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.MediaType;
import org.mockserver.verify.VerificationTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(MockServerExtension.class)
@SpringBootTest(classes = {
        RestResourceConfig.class,
        UserServiceImpl.class
})
@MockServerSettings(ports = 18000)
@TestPropertySource(properties = {
        "mmadu.userService.url=http://localhost:18000",
        "spring.security.oauth2.client.provider.mmadu.token-uri=http://localhost:18000/clients/token"
})
@ImportAutoConfiguration(OAuth2ClientAutoConfiguration.class)
@EnableAutoConfiguration
class UserServiceImplTest {
    public static final String AUTHORIZATION_HEADER = "Bearer eyJraWQiOiIxMjMiLCJhbGciOiJSUzI1NiJ9.eyJkb21haW5faWQiOiIwIiwic3ViIjoiNWVlNjFkNDE0Y2QyNTc3NWQ0MzExMzYyIiwiYXVkIjoidGVzdCIsIm5iZiI6IjE1OTIxMzkwNzMiLCJpc3MiOiJtbWFkdS5jb20iLCJleHAiOiIxNTkyMjI1NDczIiwiaWF0IjoiMTU5MjEzOTA3MyIsImF1dGhvcml0aWVzIjpbImEuKi4qKiIsInIuKi4qKiJdLCJqdGkiOiI4ODA2YjMzYy1mMDExLTRlNzUtOTI4My04YzJiMDg2MmQ3NzkiLCJjbGllbnRfaWQiOiI1ZjY1ZDBjZC02MTAzLTQ4MzgtOTc2Ni05MTMyNTg5ZTdlODkifQ.J3Bir5mmAmFsmkehDq9a_9R4c23U2cs52V8HGklIUYLR0KF2V_zsSnen8KwMPJYVNKdmJN1LDfDUNZKsPKNEXikrj4Hj4bEmdXfWHYAAXCM-tqiI0VI9TyETy_AVR3QQ_wP4DwVGKc0hioQUiyCOhLTdJtAhSed-yQDG9krTUeFnUMoMhwuMKRLgI2S_3jDose46Az84Xm2JFGMoBBe0b0waZOcidezfoUmqlTkPrU5v9kuNkS6ayLyA0l93u0DAyBQb7xVlY7bsKEiOf3-RwR038eybt8Bi64NInsmBCTLKyv9FDy5Qc_WqiQz1TmIp0v6FE5ZWdYdVLZkQJ66bdQ";
    @Autowired
    private UserService userService;
    @Value("classpath:responses/find-user-by-id.json")
    private Resource userFile;
    @Value("classpath:responses/client-credentials-token.json")
    private Resource tokenfile;
    private HttpRequest tokenRequest;

    @BeforeEach
    void setUp(MockServerClient server) throws Exception {
        tokenRequest = mockTokenApi(server);
    }

    @AfterEach
    void reset(MockServerClient server) {
        server.reset();
    }

    @Test
    void findByUserIdAndDomain(MockServerClient server) throws Exception {
        HttpRequest request = mockUserApi(server);
        NotificationUser user = userService.findByUserIdAndDomain("test", "1").block();
        assertAll(
                () -> assertEquals("test-user", user.getUsername()),
                () -> assertEquals("test", user.getId()),
                () -> assertEquals("blue", user.getProperty("favourite-colour").get()),
                () -> assertEquals("Nigeria", user.getProperty("country").get()),
                () -> server.verify(tokenRequest, VerificationTimes.exactly(1)),
                () -> server.verify(request, VerificationTimes.exactly(1))
        );
    }

    private HttpRequest mockTokenApi(MockServerClient server) throws IOException {
        HttpRequest request = request()
                .withMethod("POST")
                .withPath("/clients/token")
                .withHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8")
                .withHeader(HttpHeaders.AUTHORIZATION, "Basic bW1hZHVfYWRtaW46MTIzNDU2Nzg5MA==")
                .withHeader(HttpHeaders.ACCEPT, "application/json;charset=UTF-8")
                .withHeader(HttpHeaders.HOST, "localhost:18000")
                .withHeader(HttpHeaders.CONNECTION, "keep-alive")
                .withHeader(HttpHeaders.CONTENT_LENGTH, "49")
                .withBody("grant_type=client_credentials&scope=a.*.**+r.*.**");
        server
                .when(request)
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(
                                        IOUtils.toString(tokenfile.getInputStream(), StandardCharsets.UTF_8),
                                        MediaType.JSON_UTF_8
                                )
                );
        return request;
    }

    private HttpRequest mockUserApi(MockServerClient server) throws IOException {
        HttpRequest request = request()
                .withMethod("GET")
                .withPath("/domains/1/users/test")
                .withHeader(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER);
        server.when(
                request
        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(
                                        IOUtils.toString(userFile.getInputStream(), StandardCharsets.UTF_8),
                                        MediaType.JSON_UTF_8
                                )
                );
        return request;
    }
}