package com.mmadu.identity.providers.authorization;

import com.mmadu.identity.models.authorization.errors.AuthorizationError;
import com.mmadu.identity.models.authorization.AuthorizationResult;
import com.mmadu.identity.models.authorization.RedirectData;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationResultProcessorImplTest {
    private final AuthorizationResultProcessor processor = new AuthorizationResultProcessorImpl();
    @Mock
    private RedirectData redirectData;

    @ParameterizedTest
    @MethodSource
    void success(String redirectUri, Map<String, List<String>> params, String expectedString) {
        when(redirectData.toParams()).thenReturn(params);

        AuthorizationResult result = new AuthorizationResult();
        result.setData(redirectData);
        result.setRedirectUri(redirectUri);
        result.setComplete(true);
        String resultString = processor.processResult(result);

        assertEquals(expectedString, resultString);
    }

    private static Stream<Arguments> success() {
        return Stream.of(
                arguments("http://google.com",
                        Map.of("key1", List.of("value1"),
                                "key2", List.of("value2")
                        ),
                        "redirect:http://google.com?key1=value1&key2=value2"
                ),
                arguments("http://google.com",
                        emptyMap(),
                        "redirect:http://google.com"
                ),
                arguments("http://google.com?q=something",
                        emptyMap(),
                        "redirect:http://google.com?q=something"
                ),
                arguments("http://google.com?q=something",
                        Map.of("key1", List.of("value1"),
                                "key2", List.of("value2")
                        ),
                        "redirect:http://google.com?q=something&key1=value1&key2=value2"
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    void error(String redirectUri, AuthorizationError error, String expectedString) {
        AuthorizationResult result = new AuthorizationResult();
        result.setError(error);
        result.setRedirectUri(redirectUri);
        result.setComplete(true);
        String resultString = processor.processResult(result);

        assertEquals(expectedString, resultString);
    }

    private static Stream<Arguments> error() {
        return Stream.of(
                arguments(
                        "http://google.com",
                        genericError(),
                        "redirect:http://google.com?error_description=error&state=xyz&error=server_error&error_uri=http://uri.com"
                ),
                arguments(
                        "http://google.com?q=something",
                        genericError(),
                        "redirect:http://google.com?q=something&error_description=error&state=xyz&error=server_error&error_uri=http://uri.com"
                )
        );
    }

    private static AuthorizationError genericError() {
        AuthorizationError error = new AuthorizationError();
        error.setError("server_error");
        error.setErrorDescription("error");
        error.setErrorUri("http://uri.com");
        error.setState("xyz");
        return error;
    }
}