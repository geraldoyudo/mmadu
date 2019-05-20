package com.mmadu.service.documentation;

import static com.mmadu.service.utilities.DomainAuthenticationConstants.DOMAIN_AUTH_TOKEN_FIELD;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.service.entities.AppDomain;
import com.mmadu.service.entities.DomainConfiguration;
import com.mmadu.service.repositories.DomainConfigurationRepository;
import com.mmadu.service.service.DomainConfigurationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;

public class DomainConfigurationDocumentation extends AbstractDocumentation {

    private static final String DOMAIN_CONFIG_ID = "12333";
    private static final String DOMAIN_ID_FOR_CONFIG = "1111111111";

    @Autowired
    private DomainConfigurationRepository domainConfigurationRepository;
    @Autowired
    private DomainConfigurationService domainConfigurationService;

    @Test
    public void createADomainConfiguration() throws Exception {
        DomainConfiguration configuration = createDomainConfigurationWithConstantId();
        mockMvc.perform(post("/domainConfigurations")
                .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
                .content(objectToString(configuration))
        )
                .andExpect(status().isCreated())
                .andDo(document(DOCUMENTATION_NAME, requestFields(
                        fieldWithPath("id").description("The domain configuration ID"),
                        fieldWithPath("domainId").description("The domain ID"),
                        fieldWithPath("authenticationApiToken").description("The api token ID used to secure this domain")
                )));
    }

    private DomainConfiguration createDomainConfigurationWithConstantId(){
        DomainConfiguration configuration = new DomainConfiguration();
        configuration.setDomainId(DOMAIN_ID_FOR_CONFIG);
        configuration.setAuthenticationApiToken("1");
        configuration.setId(DOMAIN_CONFIG_ID);
        return configuration;
    }

    @Test
    public void getDomainConfigurationByID() throws Exception{
        createAndSaveDomainConfiguration();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/domainConfigurations/{domainConfigurationId}", DOMAIN_CONFIG_ID)
                .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
        )
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME, parameterFields(), domainConfigurationResponseFields()));
    }

    private DomainConfiguration createAndSaveDomainConfiguration(){
        AppDomain domain = new AppDomain();
        domain.setName("Test domain For Config");
        domain.setId(DOMAIN_ID_FOR_CONFIG);
        appDomainRepository.save(domain);
        return domainConfigurationRepository.save(createDomainConfigurationWithConstantId());
    }

    private PathParametersSnippet parameterFields() {
        return pathParameters(
                parameterWithName("domainConfigurationId").description("The domain configuration ID")
        );
    }

    private ResponseFieldsSnippet domainConfigurationResponseFields(){
        return responseFields(
                fieldWithPath("domainId").description("The domain ID)"),
                fieldWithPath("authenticationApiToken").description("The api token ID used to secure this domain"),
                subsectionWithPath("_links").type("map").description("User item resource links")
        );
    }

    @Test
    public void getDefaultDomainConfiguration() throws Exception{
        domainConfigurationService.init();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/domainConfigurations/{domainConfigurationId}", "0")
                .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
        )
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME, parameterFields(), domainConfigurationResponseFields()));
    }

    @Test
    public void getDomainConfigurationByDomainID() throws Exception{
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
    public void updateDomainConfiguration() throws Exception{
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
