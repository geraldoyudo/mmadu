package com.mmadu.identity.documentation;

import com.nimbusds.jose.util.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BatchDomainIdentityInitializationDocumentation extends AbstractDocumentation {

    @Test
    void batchCreateDomains() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/admin/domains")
                .header(HttpHeaders.AUTHORIZATION, authorization("a.global.domain_identity.initialize"))
                .contentType(MediaType.APPLICATION_JSON).content(
                        IOUtils.readInputStreamToString(
                                new ClassPathResource("/domains/sample.json").getInputStream()
                        )
                ))
                .andExpect(status().isCreated())
                .andDo(document(DOCUMENTATION_NAME, requestFields(
                        fieldWithPath("[].domainId").description("Domain id"),
                        fieldWithPath("[].authorizationCodeType").description("Authorization Code Type e.g {alphanumeric, jwt}"),
                        fieldWithPath("[].authorizationCodeTTLSeconds").description("Authorization code validity period"),
                        fieldWithPath("[].maxAuthorizationTTLSeconds").description("Maximum validity period for every authorization"),
                        subsectionWithPath("[].authorizationCodeTypeProperties").description("Properties of the authorization code"),
                        fieldWithPath("[].refreshTokenEnabled").description("Generate refresh token for refresh token enabled grants"),
                        subsectionWithPath("[].refreshTokenProperties").description("Properties of the refresh token"),
                        fieldWithPath("[].accessTokenProvider").description("Provider for generating access token e.g (jwt)"),
                        subsectionWithPath("[].accessTokenProperties").description("Access token properties"),
                        fieldWithPath("[].issuerId").description("Issuer id. Will be the used as the iss field in jwt tokens"),
                        subsectionWithPath("[].clients").description("List of predefined clients"),
                        subsectionWithPath("[].clientInstances").description("List of predefined client instances"),
                        subsectionWithPath("[].resources").description("List of predefined resources"),
                        subsectionWithPath("[].scopes").description("List of predefined scopes")
                )));
    }

}
