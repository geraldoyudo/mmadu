package com.mmadu.notifications.defaultrestsms;

import com.mmadu.notifications.endpoint.NotificationProviderRegistration;
import com.mmadu.notifications.endpoint.models.NotificationContext;
import com.mmadu.notifications.endpoint.models.NotificationMessage;
import com.mmadu.notifications.endpoint.models.NotificationMessageHeaders;
import com.mmadu.notifications.endpoint.models.NotificationUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.MediaType;
import org.mockserver.verify.VerificationTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.mmadu.notifications.defaultrestsms.DefaultRestSMSAutoConfigurationTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(MockServerExtension.class)
@MockServerSettings(ports = 18000)
@SpringBootTest(classes = DefaultRestSMSAutoConfiguration.class)
@TestPropertySource(properties = {
        "mmadu.notifications.default-rest-sms.endpoint-url=" + SMS_API,
        "mmadu.notifications.default-rest-sms.sender-id=" + TEST_SENDER,
        "mmadu.notifications.default-rest-sms.password=" + TEST_PASSWORD,
        "mmadu.notifications.default-rest-sms.username=" + TEST_USER,
}
)
class DefaultRestSMSAutoConfigurationTest {
    public static final String SMS_API = "http://localhost:18000/api";
    public static final String TEST_USER = "test_user";
    public static final String TEST_PASSWORD = "test_password";
    public static final String TEST_SENDER = "test_sender";

    @Autowired
    private NotificationProviderRegistration registration;

    @Mock
    private NotificationMessage message;
    @Mock
    private NotificationMessageHeaders headers;
    @Mock
    private NotificationContext context;
    @Mock
    private NotificationUser user;

    @AfterEach
    void reset(MockServerClient server) {
        server.reset();
    }

    @Test
    void sendGenericMessage(MockServerClient server) {
        HttpRequest request = mockScenario(server);
        when(headers.get("destination")).thenReturn(Collections.singletonList("+2348078223323"));
        registration.getProvider().process(message)
                .block();
        assertAll(
                () -> assertTrue(registration.getProvider().supportsMessage(message)),
                () -> server.verify(request, VerificationTimes.exactly(1))
        );
    }

    private HttpRequest mockScenario(MockServerClient server) {
        HttpRequest request = mockSMSApiOK(server, "test message value-1", "2348078223323");
        when(message.getHeaders()).thenReturn(headers);
        when(message.getContext()).thenReturn(context);
        when(context.getAsMap()).thenReturn(Map.of("param", "value-1"));
        when(message.getMessageTemplate()).thenReturn("test message $param");
        when(message.getType()).thenReturn("sms");
        return request;
    }

    @Test
    void sendUserMessage(MockServerClient server) {
        HttpRequest request = mockScenario(server);
        when(context.getUser()).thenReturn(Optional.of(user));
        when(user.getProperty("phoneNumber")).thenReturn(Optional.of("+2348078223323"));
        registration.getProvider().process(message)
                .block();
        assertAll(
                () -> assertTrue(registration.getProvider().supportsMessage(message)),
                () -> server.verify(request, VerificationTimes.exactly(1))
        );
    }

    private HttpRequest mockSMSApiOK(MockServerClient server, String message, String mobiles) {
        HttpRequest request = HttpRequest.request()
                .withMethod("GET")
                .withPath("/api")
                .withQueryStringParameter("username", TEST_USER)
                .withQueryStringParameter("password", TEST_PASSWORD)
                .withQueryStringParameter("sender", TEST_SENDER)
                .withQueryStringParameter("message", message)
                .withQueryStringParameter("mobiles", mobiles);
        server
                .when(request)
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody("{\"status\":\"OK\",\"count\":1,\"price\":2}", MediaType.APPLICATION_JSON)
                );
        return request;
    }

    @Test
    void registrationProviderShouldHaveCorrectId() {
        assertEquals("rest-sms", registration.getId());
    }
}