package com.mmadu.notifications.sendgrid.model;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;


class SendgridRequestTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testDynamicTemplateRequestSerialization() throws Exception {
        SendgridRequest request = SendgridRequest.builder()
                .from(SendgridRequest.Address.builder().email("admin@sacraliturgios.com").build())
                .templateId("d-197bd78523584a3a8ff97424c98a3269")
                .personalization(
                        SendgridRequest.Personalization.builder()
                                .to(SendgridRequest.Address.builder().email("gerald.oyudo@gmail.com").build())
                                .data(Map.of("sample", "Boss"))
                                .build()
                ).build();

        assertTrue(
                mapper.readTree(SendgridRequestTest.class.getClassLoader()
                        .getResourceAsStream("request/sendgrid-dynamic-request.json"))
                        .equals(mapper.valueToTree(request))

        );
    }
}