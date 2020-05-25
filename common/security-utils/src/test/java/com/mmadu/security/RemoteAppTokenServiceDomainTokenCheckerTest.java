package com.mmadu.security;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(WiremockExtension.class)
class RemoteAppTokenServiceDomainTokenCheckerTest {
    private static final String ADMIN_KEY = "3333";
    public static final String TOKEN_ID = "1234";
    public static final String DOMAIN_ID = "1111";


    private RemoteAppTokenServiceDomainTokenChecker tokenChecker = new RemoteAppTokenServiceDomainTokenChecker();

    @BeforeEach
    void setUp() {
        tokenChecker.setTokenServiceUrl("http://localhost:19998");
        tokenChecker.setAdminKey(ADMIN_KEY);
    }

    @Test
    void givenTokenMatchesThenReturnTrue() {
        mockCheckDomainApiToReturn("{\"matches\": true}");
        assertThat(tokenChecker.checkIfTokenMatchesDomainToken(TOKEN_ID, DOMAIN_ID), CoreMatchers.is(true));
    }

    private void mockCheckDomainApiToReturn(String s) {
        stubFor(post(urlPathEqualTo("/token/checkDomainToken"))
                .withRequestBody(matchingJsonPath(
                        "$.token", WireMock.equalTo(TOKEN_ID))
                )
                .withRequestBody(matchingJsonPath(
                        "$.domainId", WireMock.equalTo(DOMAIN_ID))
                )
                .withHeader("domain-auth-token", WireMock.equalTo(ADMIN_KEY))
                .willReturn(aResponse()
                        .withBody(s)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)));
    }

    @Test
    void givenTokenNotMatchesThenReturnFalse() {
        mockCheckDomainApiToReturn("{\"matches\": false}");
        assertThat(tokenChecker.checkIfTokenMatchesDomainToken(TOKEN_ID, DOMAIN_ID), CoreMatchers.is(false));
    }
}