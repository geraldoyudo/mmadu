package com.mmadu.service.documentation;

import com.mmadu.service.models.AuthenticateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static com.mmadu.service.models.AuthenticationStatus.AUTHENTICATED;
import static com.mmadu.service.utilities.DomainAuthenticationConstants.DOMAIN_AUTH_TOKEN_FIELD;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationDocumentation extends AbstractDocumentation {

    @BeforeEach
    void setUp() {
        appUserRepository.save(createAppUserWithConstantId());
        appDomainRepository.save(createDomain());
    }

    @Test
    void authentication() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/domains/{domainId}/authenticate",
                USER_DOMAIN_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ADMIN_TOKEN)
                .contentType(MediaType.APPLICATION_JSON).content(objectToString(
                        new AuthenticateRequest(USERNAME, USER_PASSWORD)
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(AUTHENTICATED.name()))
                .andDo(document(DOCUMENTATION_NAME, requestFields(
                        fieldWithPath("username").description("The user identification"),
                        fieldWithPath("password").description("The user's password")
                ), responseFields(
                        fieldWithPath("status").description("The authentication status. One of the following: "
                                + "AUTHENTICATED, USERNAME_INVALID, PASSWORD_INVALID")
                ), pathParameters(
                        parameterWithName("domainId").description("The user authentication domain id")
                )));
    }
}
