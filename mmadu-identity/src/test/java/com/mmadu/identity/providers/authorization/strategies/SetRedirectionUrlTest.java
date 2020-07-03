package com.mmadu.identity.providers.authorization.strategies;

import com.mmadu.identity.exceptions.AuthorizationException;
import com.mmadu.identity.models.authorization.AuthorizationContext;
import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.models.authorization.AuthorizationResponse;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.services.client.MmaduClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = SetRedirectionUrl.class)
class SetRedirectionUrlTest {
    public static final String TEST_CLIENT_IDENTIFIER = "test-client";
    @Autowired
    private AuthorizationStrategy setRedirectStrategy;

    @MockBean
    private MmaduClientService mmaduClientService;

    @Mock
    private MmaduClient mmaduClient;
    @Mock
    private AuthorizationRequest request;
    @Mock
    private AuthorizationResponse response;
    @Mock
    private AuthorizationContext context;

    @Captor
    public ArgumentCaptor<String> redirectUriCaptor;

    @BeforeEach
    void setUp() {
        when(mmaduClientService.loadClientByIdentifier(TEST_CLIENT_IDENTIFIER)).thenReturn(Optional.of(mmaduClient));
    }

    @ParameterizedTest
    @MethodSource("authorizeSourcePositiveMatch")
    void givenValidRedirectUrisWhenAuthorizeThenSetRedirectUri(List<String> clientRedirectUris, String requestRedirectUri) {
        setUpClientAndArguments(clientRedirectUris, requestRedirectUri);
        doNothing().when(context).setRedirectUri(redirectUriCaptor.capture(), eq(true));

        setRedirectStrategy.authorize(request, response, context);
        verify(context, times(1)).setRedirectUri(requestRedirectUri, true);
    }

    private void setUpClientAndArguments(List<String> clientRedirectUris, String requestRedirectUri) {
        when(mmaduClient.getRedirectUris()).thenReturn(clientRedirectUris);
        when(request.getRedirect_uri()).thenReturn(requestRedirectUri);
        when(request.getClient_id()).thenReturn(TEST_CLIENT_IDENTIFIER);
    }

    private static Stream<Arguments> authorizeSourcePositiveMatch() {
        return Stream.of(
                arguments(
                        List.of("http://local-1.com/callback", "http://local-1.com/oauth/callback"),
                        "http://local-1.com/callback"
                ),
                arguments(
                        emptyList(),
                        "http://local-1.com/callback"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("authorizeSourceNegativeMatch")
    void givenInvalidRedirectUrisWhenAuthorizeThenThrowAuthorizationException(List<String> clientRedirectUris, String requestRedirectUri) {
        setUpClientAndArguments(clientRedirectUris, requestRedirectUri);

        assertThrows(AuthorizationException.class, () -> setRedirectStrategy.authorize(request, response, context));
    }

    private static Stream<Arguments> authorizeSourceNegativeMatch() {
        return Stream.of(
                arguments(
                        List.of("http://local-1.com/callback", "http://local-1.com/oauth/callback"),
                        "http://local-1.com/callback1"
                ),
                arguments(
                        List.of("http://local-1.com/callback", "http://local-1.com/oauth/callback"),
                        ""
                ),
                arguments(
                        List.of("http://local-1.com/callback", "http://local-1.com/oauth/callback"),
                        null
                ),
                arguments(
                        emptyList(),
                        null
                ),
                arguments(
                        emptyList(),
                        ""
                )
        );
    }
}