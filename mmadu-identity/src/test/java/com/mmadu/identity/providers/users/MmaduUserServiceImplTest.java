package com.mmadu.identity.providers.users;

import com.mmadu.identity.config.RestResourceConfig;
import com.mmadu.identity.models.user.MmaduUser;
import com.mmadu.identity.services.user.MmaduUserService;
import com.mmadu.identity.services.user.MmaduUserServiceImpl;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.matchers.MatchType;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;

@ExtendWith(MockServerExtension.class)
@SpringBootTest(classes = {
        RestResourceConfig.class,
        MmaduUserServiceImpl.class
})
@MockServerSettings(ports = 18000)
@TestPropertySource(properties = {
        "mmadu.userService.url=http://localhost:18000",
        "spring.security.oauth2.client.provider.mmadu.token-uri=http://localhost:18000/clients/token"
})
@ImportAutoConfiguration(OAuth2ClientAutoConfiguration.class)
@EnableAutoConfiguration
class MmaduUserServiceImplTest {
    public static final String AUTHORIZATION_HEADER = "Bearer eyJraWQiOiIxMjMiLCJhbGciOiJSUzI1NiJ9.eyJkb21haW5faWQiOiIwIiwic3ViIjoiNWVlNjFkNDE0Y2QyNTc3NWQ0MzExMzYyIiwiYXVkIjoidGVzdCIsIm5iZiI6IjE1OTIxMzkwNzMiLCJpc3MiOiJtbWFkdS5jb20iLCJleHAiOiIxNTkyMjI1NDczIiwiaWF0IjoiMTU5MjEzOTA3MyIsImF1dGhvcml0aWVzIjpbImEuKi4qKiIsInIuKi4qKiJdLCJqdGkiOiI4ODA2YjMzYy1mMDExLTRlNzUtOTI4My04YzJiMDg2MmQ3NzkiLCJjbGllbnRfaWQiOiI1ZjY1ZDBjZC02MTAzLTQ4MzgtOTc2Ni05MTMyNTg5ZTdlODkifQ.J3Bir5mmAmFsmkehDq9a_9R4c23U2cs52V8HGklIUYLR0KF2V_zsSnen8KwMPJYVNKdmJN1LDfDUNZKsPKNEXikrj4Hj4bEmdXfWHYAAXCM-tqiI0VI9TyETy_AVR3QQ_wP4DwVGKc0hioQUiyCOhLTdJtAhSed-yQDG9krTUeFnUMoMhwuMKRLgI2S_3jDose46Az84Xm2JFGMoBBe0b0waZOcidezfoUmqlTkPrU5v9kuNkS6ayLyA0l93u0DAyBQb7xVlY7bsKEiOf3-RwR038eybt8Bi64NInsmBCTLKyv9FDy5Qc_WqiQz1TmIp0v6FE5ZWdYdVLZkQJ66bdQ";
    @Autowired
    private MmaduUserService userService;
    @Value("classpath:responses/get-user-by-username-and-domain.json")
    private Resource userFile;
    @Value("classpath:responses/client-credentials-token.json")
    private Resource tokenfile;
    @Value("classpath:requests/user-authentication-request.json")
    private Resource userAuthenticateRequest;
    @Value("classpath:responses/user-authentication-response.json")
    private Resource userAuthenticateResponse;
    @Value("classpath:responses/user-authentication-username-invalid.json")
    private Resource invalidUsernameResponse;
    @Value("classpath:responses/user-authentication-password-invalid.json")
    private Resource invalidPasswordResponse;
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
    void findById(MockServerClient server) throws Exception {
        HttpRequest request = mockUserApi(server);
        MmaduUser user = userService.loadUserByUsernameAndDomainId("test", "1").get();
        assertAll(
                () -> assertEquals("test", user.getUsername()),
                () -> assertEquals("1", user.getDomainId()),
                () -> assertEquals("0c76dbb1c4234404be8b1b051937de77", user.getId()),
                () -> assertEquals(List.of("admin"), user.getRoles()),
                () -> assertEquals(List.of("edit"), user.getAuthorities()),
                () -> assertEquals(List.of("test", "sample"), user.getGroups()),
                () -> server.verify(tokenRequest, VerificationTimes.exactly(1)),
                () -> server.verify(request, VerificationTimes.exactly(1))
        );
    }

    private HttpRequest mockTokenApi(MockServerClient server) throws IOException {
        HttpRequest request = request()
                .withMethod("POST")
                .withPath("/clients/token")
                .withHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8")
                .withHeader(HttpHeaders.AUTHORIZATION, "Basic NWY2NWQwY2QtNjEwMy00ODM4LTk3NjYtOTEzMjU4OWU3ZTg5OjExMzIzMjMyMw==")
                .withHeader(HttpHeaders.USER_AGENT, "Java/14.0.1")
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
                .withPath("/domains/1/users/load")
                .withQueryStringParameter("username", "test")
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

    @Test
    void whenUserNotPresentWhenFindByIdReturnEmptyOptional(MockServerClient server) throws Exception {
        HttpRequest request = mockNotFound(server);
        Optional<MmaduUser> user = userService.loadUserByUsernameAndDomainId("test", "1");
        assertAll(
                () -> assertTrue(user.isEmpty()),
                () -> server.verify(tokenRequest, VerificationTimes.exactly(1)),
                () -> server.verify(request, VerificationTimes.exactly(1))
        );
    }

    private HttpRequest mockNotFound(MockServerClient server) throws IOException {
        HttpRequest request = request()
                .withMethod("GET")
                .withPath("/domains/1/users/load")
                .withQueryStringParameter("username", "test")
                .withHeader(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER);
        server.when(
                request
        )
                .respond(
                        response()
                                .withStatusCode(404)
                );
        return request;
    }

    @Test
    void authenticate(MockServerClient server) throws Exception {
        HttpRequest request = mockAuthenticateApi(server, userAuthenticateResponse);
        userService.authenticate("1", "user", "password");
        server.verify(request, VerificationTimes.once());
    }

    private HttpRequest mockAuthenticateApi(MockServerClient server, Resource response) throws Exception {
        HttpRequest request = request()
                .withMethod("POST")
                .withPath("/domains/1/authenticate")
                .withBody(
                        json(
                                IOUtils.toString(userAuthenticateRequest.getInputStream(), StandardCharsets.UTF_8),
                                MatchType.STRICT
                        )
                )
                .withHeader(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER);
        server.when(
                request
        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(
                                        IOUtils.toString(response.getInputStream(), StandardCharsets.UTF_8),
                                        MediaType.JSON_UTF_8
                                )
                );
        return request;
    }

    @Test
    void givenInvalidPasswordThenThrowBadCredentialsException(MockServerClient server) throws Exception {
        HttpRequest request = mockAuthenticateApi(server, invalidPasswordResponse);
        assertThrows(BadCredentialsException.class,
                () -> userService.authenticate("1", "user", "password"));
        server.verify(tokenRequest, VerificationTimes.exactly(1));
        server.verify(request, VerificationTimes.once());
    }

    @Test
    void givenInvalidUsernameThenThrowBadCredentialsException(MockServerClient server) throws Exception {
        HttpRequest request = mockAuthenticateApi(server, invalidUsernameResponse);
        assertThrows(UsernameNotFoundException.class,
                () -> userService.authenticate("1", "user", "password"));
        server.verify(tokenRequest, VerificationTimes.exactly(1));
        server.verify(request, VerificationTimes.once());
    }
}