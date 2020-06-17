package com.mmadu.service.documentation;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.service.entities.AppDomain;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DomainManagementDocumentation extends AbstractDocumentation {

    private static final String DOMAIN_NAME = "new-domain";
    private static final String NEW_DOMAIN_ID = "00111111";

    @Test
    void createADomain() throws Exception {
        mockMvc.perform(post("/appDomains")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ADMIN_TOKEN)
                .content(objectToString(createConstantDomain()))
        ).andExpect(status().isCreated())
                .andDo(document(DOCUMENTATION_NAME, requestFields(
                        fieldWithPath("name").description("The domain name"),
                        fieldWithPath("id").optional().description("ID of the domain (optional, auto-generated)")
                )));
    }

    private AppDomain createConstantDomain() {
        AppDomain domain = new AppDomain();
        domain.setName(DOMAIN_NAME);
        domain.setId(NEW_DOMAIN_ID);
        return domain;
    }

    @Test
    void gettingAllDomains() throws Exception {
        mockMvc.perform(get("/appDomains")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ADMIN_TOKEN)
        )
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME, domainsResponseFields()));
    }

    private ResponseFieldsSnippet domainsResponseFields() {
        return responseFields(
                fieldWithPath("_embedded.appDomains.[].name").description("The name of the domain"),
                subsectionWithPath("_embedded.appDomains.[]._links").type("map")
                        .description("Domain item resource links"),
                subsectionWithPath("_links").type("map").description("Resource links"),
                subsectionWithPath("page").type("map").description("Page information")
        );
    }

    @Test
    void getADomainById() throws Exception {
        createAndSaveDomain();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/appDomains/{domainId}", NEW_DOMAIN_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ADMIN_TOKEN)
        )
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("domainId").description("The domain ID")
                        ), domainResponseFields()));
    }

    private void createAndSaveDomain() {
        appDomainRepository.save(createConstantDomain());
    }

    private ResponseFieldsSnippet domainResponseFields() {
        return responseFields(
                fieldWithPath("name").description("The domain name"),
                subsectionWithPath("_links").type("map").description("Domain item resource links")
        );
    }

    @Test
    void deletingADomainById() throws Exception {
        createAndSaveDomain();
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/appDomains/{domainId}", NEW_DOMAIN_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ADMIN_TOKEN)
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("domainId").description("The domain ID")
                        )));
    }

    @Test
    void updatingADomain() throws Exception {
        createAndSaveDomain();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("name", "changed-name");
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/appDomains/{domainId}", NEW_DOMAIN_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ADMIN_TOKEN)
                .content(objectNode.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("domainId").description("The domain ID")
                        )));
    }
}
