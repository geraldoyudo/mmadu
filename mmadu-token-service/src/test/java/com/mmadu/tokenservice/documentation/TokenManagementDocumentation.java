package com.mmadu.tokenservice.documentation;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TokenManagementDocumentation extends AbstractDocumentation {

    private static final String TOKEN_ID = "1";
    private static final String DOMAIN_ID_FOR_CONFIG = "1111111111";

    @Test
    public void generateToken() throws Exception {
        mockMvc.perform(get("/token/generate")
                .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
        )
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME, tokenResponseFields()));

    }

    private ResponseFieldsSnippet tokenResponseFields() {
        return responseFields(
                fieldWithPath("id").description("The token ID"),
                fieldWithPath("value")
                        .description("The token value (Encrypted under master key if encryption is enabled)")
        );
    }

    @Test
    public void retrieveToken() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/token/retrieve/{tokenId}", TOKEN_ID)
                .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
        ).andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME, parameterFields(), tokenResponseFields()));
    }

    private PathParametersSnippet parameterFields() {
        return pathParameters(
                parameterWithName("tokenId").description("The token ID")
        );
    }

    @Test
    public void resetToken() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/token/reset/{tokenId}", TOKEN_ID)
                .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
        ).andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME, parameterFields(), tokenResponseFields()));
    }

    @Test
    public void setAuthTokenForDomain() throws Exception {
        mockMvc.perform(
                post("/token/setDomainAuthToken")
                        .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
                        .content(
                                objectMapper.createObjectNode()
                                        .put("tokenId", TOKEN_ID)
                                        .put("domainId", DOMAIN_ID_FOR_CONFIG)
                                        .toString()
                        )
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void getAuthTokenForDomain() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/token/domainAuth/{domainId}",
                DOMAIN_ID_FOR_CONFIG)
                .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
        ).andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("domainId").description("The domain ID")
                        ),
                        responseFields(
                                fieldWithPath("tokenId").description("The token ID"),
                                fieldWithPath("domainId").description("The domain ID")
                        )));
    }
}
