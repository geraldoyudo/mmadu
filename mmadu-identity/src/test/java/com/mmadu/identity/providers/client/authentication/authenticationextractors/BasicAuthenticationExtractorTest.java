package com.mmadu.identity.providers.client.authentication.authenticationextractors;

import com.mmadu.identity.entities.ClientSecretCredentials;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.providers.client.authentication.MmaduClientAuthenticationToken;
import com.mmadu.identity.services.client.MmaduClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasicAuthenticationExtractorTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private MmaduClient mmaduClient;
    @Mock
    private MmaduClientService mmaduClientService;

    @InjectMocks
    private final BasicAuthenticationExtractor authenticationExtractor = new BasicAuthenticationExtractor();

    private final ClientSecretCredentials credentials = new ClientSecretCredentials("1234");

    @Test
    void givenValidBasicAuthenticationAndClientFoundReturnProperAuthorization() {
        String authorization = "Basic MTExMToxMjM0";
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorization);
        when(mmaduClientService.loadClientByIdentifier("1111")).thenReturn(Optional.of(mmaduClient));
        when(mmaduClient.getCredentials()).thenReturn(credentials);
        Optional<MmaduClientAuthenticationToken> token = authenticationExtractor.extractAuthentication(request);
        assertTrue(token.isPresent());
        assertEquals(mmaduClient, token.get().getPrincipal());
    }

    @Test
    void givenValidBasicAuthenticationAndClientNotFoundReturnEmpty() {
        String authorization = "Basic MTExMToxMjM0";
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorization);
        when(mmaduClientService.loadClientByIdentifier("1111")).thenReturn(Optional.empty());
        assertTrue(authenticationExtractor.extractAuthentication(request).isEmpty());
    }

    @Test
    void ifHeaderIsAbsentReturnEmpty() {
        assertTrue(authenticationExtractor.extractAuthentication(request).isEmpty());
    }

    @Test
    void givenDifferentAuthenticationReturnEmpty() {
        String authorization = "Bearer MTExMToxMjM0";
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authorization);
        assertTrue(authenticationExtractor.extractAuthentication(request).isEmpty());
    }
}