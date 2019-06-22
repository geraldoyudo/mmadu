package com.mmadu.security;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class RemoteAppTokenServiceDomainTokenCheckerTest {
    private static final String ADMIN_KEY = "3333";
    public static final String TOKEN_ID = "1234";
    public static final String DOMAIN_ID = "1111";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(19998);
    @Rule
    public ErrorCollector collector = new ErrorCollector();

    private RemoteAppTokenServiceDomainTokenChecker tokenChecker = new RemoteAppTokenServiceDomainTokenChecker();

    @Before
    public void setUp(){
        tokenChecker.setTokenServiceUrl("http://localhost:19998");
        tokenChecker.setAdminKey(ADMIN_KEY);
    }

    @Test
    public void givenTokenMatchesThenReturnTrue() {
        stubFor(post(urlPathEqualTo("/token/checkDomainToken"))
                .withRequestBody(matchingJsonPath(
                        "$.tokenId", WireMock.equalTo(TOKEN_ID))
                )
                .withRequestBody(matchingJsonPath(
                        "$.domainId", WireMock.equalTo(DOMAIN_ID))
                )
                .withHeader("domain-auth-token", WireMock.equalTo(ADMIN_KEY))
                .willReturn(aResponse()
                        .withBody("{\"matches\": true}")
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)));

        assertThat(tokenChecker.checkIfTokenMatchesDomainToken(TOKEN_ID, DOMAIN_ID), CoreMatchers.is(true));
    }

    @Test
    public void givenTokenNotMatchesThenReturnFalse() {
        stubFor(post(urlPathEqualTo("/token/checkDomainToken"))
                .withRequestBody(matchingJsonPath(
                        "$.tokenId", WireMock.equalTo(TOKEN_ID))
                )
                .withRequestBody(matchingJsonPath(
                        "$.domainId", WireMock.equalTo(DOMAIN_ID))
                )
                .withHeader("domain-auth-token", WireMock.equalTo(ADMIN_KEY))
                .willReturn(aResponse()
                        .withBody("{\"matches\": false}")
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)));

        assertThat(tokenChecker.checkIfTokenMatchesDomainToken(TOKEN_ID, DOMAIN_ID), CoreMatchers.is(false));
    }
}