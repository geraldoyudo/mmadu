package com.mmadu.identity.providers.users;

import com.mmadu.identity.config.RestResourceConfig;
import com.mmadu.identity.models.domain.Domain;
import com.mmadu.identity.services.domain.DomainService;
import com.mmadu.identity.services.domain.DomainServiceImpl;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(MockServerExtension.class)
@SpringBootTest(classes = {
        RestResourceConfig.class,
        DomainServiceImpl.class
})
@MockServerSettings(ports = 18000)
@TestPropertySource(properties = {
        "mmadu.userService.url=http://localhost:18000",
        "mmadu.domainKey=2222",
        "spring.security.oauth2.client.provider.mmadu.token-uri=http://localhost:18000/clients/token"
})
@ImportAutoConfiguration(OAuth2ClientAutoConfiguration.class)
@EnableAutoConfiguration
class DomainServiceImplTest {
    public static final String AUTHORIZATION_HEADER_VALUE = "Bearer eyJraWQiOiIxMjMiLCJhbGciOiJSUzI1NiJ9.eyJkb21haW5faWQiOiIwIiwic3ViIjoiNWVlNjFkNDE0Y2QyNTc3NWQ0MzExMzYyIiwiYXVkIjoidGVzdCIsIm5iZiI6IjE1OTIxMzkwNzMiLCJpc3MiOiJtbWFkdS5jb20iLCJleHAiOiIxNTkyMjI1NDczIiwiaWF0IjoiMTU5MjEzOTA3MyIsImF1dGhvcml0aWVzIjpbImEuKi4qKiIsInIuKi4qKiJdLCJqdGkiOiI4ODA2YjMzYy1mMDExLTRlNzUtOTI4My04YzJiMDg2MmQ3NzkiLCJjbGllbnRfaWQiOiI1ZjY1ZDBjZC02MTAzLTQ4MzgtOTc2Ni05MTMyNTg5ZTdlODkifQ.J3Bir5mmAmFsmkehDq9a_9R4c23U2cs52V8HGklIUYLR0KF2V_zsSnen8KwMPJYVNKdmJN1LDfDUNZKsPKNEXikrj4Hj4bEmdXfWHYAAXCM-tqiI0VI9TyETy_AVR3QQ_wP4DwVGKc0hioQUiyCOhLTdJtAhSed-yQDG9krTUeFnUMoMhwuMKRLgI2S_3jDose46Az84Xm2JFGMoBBe0b0waZOcidezfoUmqlTkPrU5v9kuNkS6ayLyA0l93u0DAyBQb7xVlY7bsKEiOf3-RwR038eybt8Bi64NInsmBCTLKyv9FDy5Qc_WqiQz1TmIp0v6FE5ZWdYdVLZkQJ66bdQ";
    @Autowired
    private DomainService domainService;
    @Value("classpath:responses/get-domain-by-id.json")
    private Resource domainFile;
    @Value("classpath:responses/client-credentials-token.json")
    private Resource tokenfile;

    @AfterEach
    void reset(MockServerClient server) {
        server.reset();
    }

    @Test
    void findById(MockServerClient server) throws Exception {
        HttpRequest tokenRequest = mockTokenApi(server);
        HttpRequest request = mockDomainApi(server);
        Domain domain = domainService.findById("1").get();
        assertAll(
                () -> assertEquals("new-domain", domain.getName()),
                () -> server.verify(tokenRequest, VerificationTimes.once()),
                () -> server.verify(request, VerificationTimes.once())
        );
    }

    private HttpRequest mockTokenApi(MockServerClient server) throws IOException {
        HttpRequest request = request()
                .withMethod("POST")
                .withPath("/clients/token")
                .withHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8")
                .withHeader(HttpHeaders.AUTHORIZATION, "Basic bW1hZHVfYWRtaW46MTIzNDU2Nzg5MA==")
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

    private HttpRequest mockDomainApi(MockServerClient server) throws IOException {
        HttpRequest request = request()
                .withMethod("GET")
                .withPath("/appDomains/1")
                .withHeader(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE);
        server.when(
                request
        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(
                                        IOUtils.toString(domainFile.getInputStream(), StandardCharsets.UTF_8),
                                        MediaType.JSON_UTF_8
                                )
                );
        return request;
    }

    @Test
    void whenDomainNotPresentWhenFindByIdReturnEmptyOptional(MockServerClient server) throws Exception {
        HttpRequest tokenRequest = mockTokenApi(server);
        HttpRequest request = mockNotFound(server);
        Optional<Domain> domain = domainService.findById("1");
        assertAll(
                () -> assertTrue(domain.isEmpty()),
                () -> server.verify(tokenRequest, VerificationTimes.exactly(1)),
                () -> server.verify(request, VerificationTimes.exactly(1))
        );
    }

    private HttpRequest mockNotFound(MockServerClient server) throws IOException {
        HttpRequest request = request()
                .withMethod("GET")
                .withPath("/appDomains/1")
                .withHeader(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE);
        server.when(
                request
        )
                .respond(
                        response()
                                .withStatusCode(404)
                );
        return request;
    }
}