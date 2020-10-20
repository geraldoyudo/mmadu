package com.mmadu.registration.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.registration.models.propertyvalidation.ValidationAttempt;
import com.mmadu.registration.models.propertyvalidation.ValidationRequest;
import com.mmadu.registration.providers.propertyvalidation.PropertyValidator;
import com.mmadu.registration.services.MmaduUserServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.validation.Errors;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(PropertyValidationDocumentation.TestConfig.class)
public class PropertyValidationDocumentation extends AbstractDocumentation {
    public static final String TEST_PROPERTY_TYPE = "sample-property";
    public static final String TEST_DOMAIN = "test-app";
    public static final String TEST_USER = "2222";
    public static final String TEST_PROPERT_NAME = "email";
    public static final String TEST_PROPERTY_VALUE = "value-1";
    private ObjectMapper mapper = new ObjectMapper();
    @MockBean
    private MmaduUserServiceClient userServiceClient;

    @Test
    void propertyValidationFlow() throws Exception {

        when(userServiceClient.getUserProperty(TEST_DOMAIN, TEST_USER, TEST_PROPERT_NAME))
                .thenReturn(Mono.just(TEST_PROPERTY_VALUE));
        when(userServiceClient.setPropertyValidationState(TEST_DOMAIN, TEST_USER, TEST_PROPERT_NAME, true)).thenReturn(Mono.empty());
        mockMvc.perform(
                post("/propertyValidation/request")
                        .header(HttpHeaders.AUTHORIZATION, authorization("a." + TEST_DOMAIN + ".validation.property." + TEST_PROPERT_NAME + ".request"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                mapper.createObjectNode()
                                        .put("domainId", TEST_DOMAIN)
                                        .put("userId", TEST_USER)
                                        .put("key", "user|" + TEST_USER + "|" + TEST_PROPERTY_TYPE)
                                        .put("propertyName", TEST_PROPERT_NAME)
                                        .put("validationType", TEST_PROPERTY_TYPE)
                                        .putPOJO("data", Map.of("key1", "value1"))
                                        .toPrettyString()
                        )
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, requestFields(
                                propertyValidationFields()
                        ))
                );

        mockMvc.perform(
                post("/propertyValidation/attempt")
                        .header(HttpHeaders.AUTHORIZATION, authorization("a." + TEST_DOMAIN + ".validation.property." + TEST_PROPERT_NAME + ".attempt"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                mapper.createObjectNode()
                                        .put("domainId", TEST_DOMAIN)
                                        .put("userId", TEST_USER)
                                        .put("key", "user|" + TEST_USER + "|" + TEST_PROPERTY_TYPE)
                                        .put("propertyName", TEST_PROPERT_NAME)
                                        .put("validationType", TEST_PROPERTY_TYPE)
                                        .putPOJO("data", Map.of("key1", "value1"))
                                        .toPrettyString()
                        )
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, requestFields(
                                propertyValidationFields()
                        ))
                );
    }

    private static List<FieldDescriptor> propertyValidationFields() {
        return asList(
                fieldWithPath("domainId").description("The domain id"),
                fieldWithPath("userId").description("The user's external id to be validated"),
                fieldWithPath("key").description("A unique key given to this validation process"),
                fieldWithPath("propertyName").description("The user property to be validated"),
                fieldWithPath("validationType").description("The validation type to be used"),
                subsectionWithPath("data").description("Extra data needed by the validation type. (See <<property-validation-types, Property Validation Types>>)")
        );
    }

    public static class TestConfig {
        @Bean
        public PropertyValidator sampleValidator() {
            return new SamplePropertyValidator();
        }
    }

    public static class SamplePropertyValidator implements PropertyValidator {
        @Override
        public String type() {
            return TEST_PROPERTY_TYPE;
        }

        @Override
        public void validateRequest(ValidationRequest request, Errors errors) {

        }

        @Override
        public Map<String, Object> prepareForValidation(ValidationRequest request, String userProperty) {
            return emptyMap();
        }

        @Override
        public boolean validate(ValidationAttempt attempt, Map<String, Object> context) {
            return true;
        }

        @Override
        public void validateAttempt(ValidationAttempt request, Errors errors) {

        }
    }
}
