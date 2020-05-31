package com.mmadu.identity.providers.users;

import com.mmadu.identity.config.RestResourceConfig;
import com.mmadu.identity.models.users.MmaduUser;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
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
        "mmadu.domainKey=2222"
})
class MmaduUserServiceImplTest {
    @Autowired
    private MmaduUserService userService;
    @Value("classpath:responses/get-user-by-username-and-domain.json")
    private Resource userFile;
    @Value("classpath:requests/user-authentication-request.json")
    private Resource userAuthenticateRequest;
    @Value("classpath:responses/user-authentication-response.json")
    private Resource userAuthenticateResponse;
    @Value("classpath:responses/user-authentication-username-invalid.json")
    private Resource invalidUsernameResponse;
    @Value("classpath:responses/user-authentication-password-invalid.json")
    private Resource invalidPasswordResponse;

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
                () -> server.verify(request, VerificationTimes.exactly(1))
        );
    }

    private HttpRequest mockUserApi(MockServerClient server) throws IOException {
        HttpRequest request = request()
                .withMethod("GET")
                .withPath("/appUsers/search/findByUsernameAndDomainId")
                .withQueryStringParameter("username", "test")
                .withQueryStringParameter("domainId", "1")
                .withHeader("domain-auth-token", "2222");
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
                () -> server.verify(request, VerificationTimes.exactly(1))
        );
    }

    private HttpRequest mockNotFound(MockServerClient server) throws IOException {
        HttpRequest request = request()
                .withMethod("GET")
                .withPath("/appUsers/search/findByUsernameAndDomainId")
                .withQueryStringParameter("username", "test")
                .withQueryStringParameter("domainId", "1")
                .withHeader("domain-auth-token", "2222");
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
                .withHeader("domain-auth-token", "2222");
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
        server.verify(request, VerificationTimes.once());
    }

    @Test
    void givenInvalidUsernameThenThrowBadCredentialsException(MockServerClient server) throws Exception {
        HttpRequest request = mockAuthenticateApi(server, invalidUsernameResponse);
        assertThrows(UsernameNotFoundException.class,
                () -> userService.authenticate("1", "user", "password"));
        server.verify(request, VerificationTimes.once());
    }
}