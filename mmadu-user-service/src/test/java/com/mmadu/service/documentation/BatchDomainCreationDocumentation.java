package com.mmadu.service.documentation;

import com.nimbusds.jose.util.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static com.mmadu.service.models.AuthenticationStatus.AUTHENTICATED;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BatchDomainCreationDocumentation extends AbstractDocumentation {


    @Test
    void batchCreateDomains() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/domains")
                .header(HttpHeaders.AUTHORIZATION, authorization("a.global.domain.initialize"))
                .contentType(MediaType.APPLICATION_JSON).content(
                        IOUtils.readInputStreamToString(
                                new ClassPathResource("/domains/sample.json").getInputStream()
                        )
                ))
                .andExpect(status().isCreated())
                .andDo(document(DOCUMENTATION_NAME, requestFields(
                        fieldWithPath("[].id").description("Domain id"),
                        fieldWithPath("[].name").description("Domain Name"),
                        subsectionWithPath("[].users").description("List of predefined users"),
                        subsectionWithPath("[].authorities").description("List of predefined authorities"),
                        subsectionWithPath("[].roles").description("List of predefined roles"),
                        subsectionWithPath("[].groups").description("List of predefined groups (starting with parents)"),
                        subsectionWithPath("[].roleAuthorities").description("List of predefined role authority mappings"),
                        subsectionWithPath("[].userAuthorities").description("List of predefined user authority mappings"),
                        subsectionWithPath("[].userRoles").description("List of predefined user role mappings"),
                        subsectionWithPath("[].userGroups").description("List of predefined user group mappings")
                )));
    }
}
