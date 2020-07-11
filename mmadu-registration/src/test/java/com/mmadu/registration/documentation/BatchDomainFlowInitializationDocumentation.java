package com.mmadu.registration.documentation;

import com.nimbusds.jose.util.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BatchDomainFlowInitializationDocumentation extends AbstractDocumentation {

    @Test
    void batchCreateDomains() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/domains")
                .header(HttpHeaders.AUTHORIZATION, authorization("a.global.domain_flow.initialize"))
                .contentType(MediaType.APPLICATION_JSON).content(
                        IOUtils.readInputStreamToString(
                                new ClassPathResource("/requests/domains/sample.json").getInputStream()
                        )
                ))
                .andExpect(status().isCreated())
                .andDo(document(DOCUMENTATION_NAME, requestFields(
                        subsectionWithPath("fieldTypes").description("Field Type List"),
                        fieldWithPath("domains.[].domainId").description("Domain ID to be configured"),
                        subsectionWithPath("domains.[].registrationProfiles").description("Registration proiles"),
                        subsectionWithPath("domains.[].fields").description("List of fields in registration form")
                )));
    }

}