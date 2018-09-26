package com.mmadu.service.documentation;

import static com.mmadu.service.models.AuthenticationStatus.AUTHENTICATED;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mmadu.service.models.AuthenticateRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;

public class AuthenticationDocumentation extends AbstractDocumentation {

    @Before
    public void setUp(){
        appUserRepository.save(createAppUserWithConstantId());
        appDomainRepository.save(createDomain());
    }

    @Test
    public void authentication() throws Exception {
        this.mockMvc.perform(post("/authenticate")
            .contentType(MediaType.APPLICATION_JSON).content(objectToString(
                                AuthenticateRequest.builder().username(USERNAME).password(USER_PASSWORD)
                                        .domain(USER_DOMAIN_ID)
                                .build()
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(AUTHENTICATED.name()))
                .andDo(document(DOCUMENTATION_NAME, requestFields(
                        fieldWithPath("username").description("The user identification"),
                        fieldWithPath("domain").description("The user authentication domain id"),
                        fieldWithPath("password").description("The user's password")
                ), responseFields(
                        fieldWithPath("status").description("The authentication status. One of the following: "
                                + "AUTHENTICATED, USERNAME_INVALID, PASSWORD_INVALID")
                )));
    }
}
