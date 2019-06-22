package com.mmadu.tokenservice.documentation;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.tokenservice.entities.DomainConfiguration;
import com.mmadu.tokenservice.repositories.DomainConfigurationRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DomainConfigurationDocumentation extends AbstractDocumentation {

    private static final String DOMAIN_CONFIG_ID = "12333";
    private static final String DOMAIN_ID_FOR_CONFIG = "1111111111";
    private static final String USER_DOMAIN_ID = "0";

    @Autowired
    private DomainConfigurationRepository domainConfigurationRepository;

    @Test
    public void createADomainConfiguration() throws Exception {
        DomainConfiguration configuration = createDomainConfigurationWithConstantId();
        mockMvc.perform(post("/domainConfigurations")
                .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(configuration))
        )
                .andExpect(status().isCreated())
                .andDo(document(DOCUMENTATION_NAME, requestFields(
                        fieldWithPath("id").description("The domain configuration ID"),
                        fieldWithPath("domainId").description("The domain ID"),
                        fieldWithPath("authenticationApiToken").description("The api token ID used to secure this domain")
                )));
    }

    private DomainConfiguration createDomainConfigurationWithConstantId() {
        DomainConfiguration configuration = new DomainConfiguration();
        configuration.setDomainId(DOMAIN_ID_FOR_CONFIG);
        configuration.setAuthenticationApiToken("1");
        configuration.setId(DOMAIN_CONFIG_ID);
        return configuration;
    }

    @Test
    public void getDomainConfigurationByID() throws Exception {
        createAndSaveDomainConfiguration();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/domainConfigurations/{domainConfigurationId}", DOMAIN_CONFIG_ID)
                .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
        )
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME, parameterFields(), domainConfigurationResponseFields()));
    }

    private DomainConfiguration createAndSaveDomainConfiguration() {
        return domainConfigurationRepository.save(createDomainConfigurationWithConstantId());
    }

    private PathParametersSnippet parameterFields() {
        return pathParameters(
                parameterWithName("domainConfigurationId").description("The domain configuration ID")
        );
    }

    private ResponseFieldsSnippet domainConfigurationResponseFields() {
        return responseFields(
                fieldWithPath("domainId").description("The domain ID)"),
                fieldWithPath("authenticationApiToken").description("The api token ID used to secure this domain"),
                subsectionWithPath("_links").type("map").description("User item resource links")
        );
    }

    @Test
    public void getDefaultDomainConfiguration() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/domainConfigurations/{domainConfigurationId}", "0")
                .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
        )
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME, parameterFields(), domainConfigurationResponseFields()));
    }

    @Test
    public void getDomainConfigurationByDomainID() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/domainConfigurations/search/findByDomainId")
                .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
                .param("domainId", USER_DOMAIN_ID)
        )
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME, domainConfigurationResponseFields(),
                        requestParameters(
                                parameterWithName("domainId").description("The domain id")
                        )));
    }

    @Test
    public void updateDomainConfiguration() throws Exception {
        createAndSaveDomainConfiguration();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("authenticationApiToken", "2");
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/domainConfigurations/{domainConfigurationId}",
                DOMAIN_CONFIG_ID)
                .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
                .content(objectNode.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        parameterFields()));
    }

    @Test
    public void deleteDomainConfiguration() throws Exception {
        createAndSaveDomainConfiguration();
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/domainConfigurations/{domainConfigurationId}",
                DOMAIN_CONFIG_ID)
                .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME, parameterFields()));
    }
}
