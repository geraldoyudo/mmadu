package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.endpoint.models.NotificationMessage;
import com.mmadu.notifications.endpoint.models.NotificationUser;
import com.mmadu.notifications.service.models.DefaultNotificationHeaders;
import com.mmadu.notifications.service.models.DefaultNotificationMessage;
import com.mmadu.notifications.service.models.MapBasedNotificationContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = SPELNotificationRuleMatcher.class)
class SPELNotificationRuleMatcherTest {
    @Autowired
    private NotificationRuleMatcher ruleMatcher;

    @ParameterizedTest
    @CsvSource({
            "type == 'email',true",
            "type == 'sms',false",
            "headers.getOne('header1').get() == 'value1',true",
            "headers.getOne('header1').get() == 'value2',false",
            "context.get('param').get() == 'value',true",
            "context.get('param').get() == 'value1',false",
    })
    void notificationRuleMatchingTest(String expression, boolean match) {
        NotificationMessage message = DefaultNotificationMessage.builder()
                .messageTemplate("template-1")
                .context(new MapBasedNotificationContext(
                        Map.of("param", "value"),
                        new NotificationUser()
                ))
                .headers(new DefaultNotificationHeaders(
                        Map.of("header1", "value1")
                ))
                .type("email")
                .build();
        assertEquals(match, ruleMatcher.matchesRule(expression, message));
    }
}