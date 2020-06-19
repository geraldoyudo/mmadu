package com.mmadu.identity.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.providers.authorization.code.AlphaNumericCodeGenerator;
import com.mmadu.identity.repositories.DomainIdentityConfigurationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DomainIdentityConfigurationDocumentation extends AbstractDocumentation {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DomainIdentityConfigurationRepository domainIdentityConfigurationRepository;

    @AfterEach
    public void reset() {
        domainIdentityConfigurationRepository.deleteAll();
    }

    @Test
    void createNewDomainIdentityConfiguration() throws Exception {
        mockMvc.perform(
                post("/admin/repo/domainIdentityConfigurations")
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.identity_config.create"))
                        .content(newDomainIdentityConfigurationRequest())
        ).andExpect(status().isCreated())
                .andDo(
                        document(DOCUMENTATION_NAME, requestFields(
                                domainIdentityConfigurationFields()
                        ))
                );
    }

    private String newDomainIdentityConfigurationRequest() {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("domainId", DOMAIN_ID)
                .put("authorizationCodeType", AlphaNumericCodeGenerator.TYPE)
                .put("authorizationCodeTTLSeconds", 600L)
                .put("maxAuthorizationTTLSeconds", 24 * 60 * 60L)
                .put("domainId", DOMAIN_ID)
                .put("refreshTokenEnabled", true)
                .put("accessTokenProvider", "jwt")
                .put("refreshTokenProvider", "alphanumeric")
                .put("issuerId", "mmadu.com");
        node.putObject("authorizationCodeTypeProperties");
        node.putObject("accessTokenProperties");
        node.putObject("refreshTokenProperties");
        return node.toPrettyString();
    }

    private static List<FieldDescriptor> domainIdentityConfigurationFields() {
        return asList(
                fieldWithPath("id").type("string").optional().description("Domain Identity Configuration ID"),
                fieldWithPath("domainId").description("The Domain Id"),
                fieldWithPath("authorizationCodeType").description("The format of authorization code generated in authorization_code grant type flow"),
                subsectionWithPath("authorizationCodeTypeProperties").description("Properties for customizing the authorization code generation").optional(),
                fieldWithPath("authorizationCodeTTLSeconds").description("Validity of the authorization code in seconds"),
                fieldWithPath("maxAuthorizationTTLSeconds").description("Maximum validity of Granted Authorization in seconds (regardless of any authorization type)"),
                fieldWithPath("refreshTokenEnabled").description("If enabled, refresh token will be issued if the flow supports it."),
                fieldWithPath("accessTokenProvider").description("The provider used for generating access tokens"),
                subsectionWithPath("accessTokenProperties").description("Properties for customizing the access token generation").optional(),
                fieldWithPath("refreshTokenProvider").description("The provider used for generating refresh tokens"),
                subsectionWithPath("refreshTokenProperties").description("Properties for customizing the refresh token generation").optional(),
                fieldWithPath("issuerId").description("issuer id of the domain")
        );
    }

    @Test
    void getDomainIdentityConfigurationById() throws Exception {
        DomainIdentityConfiguration configuration = newDomainIdentityConfiguration();
        configuration = domainIdentityConfigurationRepository.save(configuration);
        mockMvc.perform(
                RestDocumentationRequestBuilders.get(
                        "/admin/repo/domainIdentityConfigurations/{domainIdentityConfigurationId}",
                        configuration.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.global.identity_config.read"))
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                domainIdentityConfigurationIdParameter()
                        ), relaxedResponseFields(
                                domainIdentityConfigurationFields()
                        ))
                );
    }

    private DomainIdentityConfiguration newDomainIdentityConfiguration() throws com.fasterxml.jackson.core.JsonProcessingException {
        return objectMapper.readValue(newDomainIdentityConfigurationRequest(), DomainIdentityConfiguration.class);
    }

    private ParameterDescriptor domainIdentityConfigurationIdParameter() {
        return parameterWithName("domainIdentityConfigurationId").description("The domain identity configuration id");
    }

    @Test
    public void getDomainIdentityConfigurationByDomainId() throws Exception {
        DomainIdentityConfiguration configuration = domainIdentityConfigurationRepository.save(newDomainIdentityConfiguration());
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/admin/repo/domainIdentityConfigurations/search/findByDomainId")
                        .param("domainId", configuration.getDomainId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.identity_config.read"))
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, requestParameters(
                                parameterWithName("domainId").description("Domain ID")
                        ), relaxedResponseFields(
                                domainIdentityConfigurationFields()
                        ))
                );
    }

    @Test
    public void getAllDomainIdentityConfigurations() throws Exception {
        domainIdentityConfigurationRepository.save(newDomainIdentityConfiguration());
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/admin/repo/domainIdentityConfigurations")
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.global.identity_config.read"))
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, relaxedResponseFields(
                                domainIdentityConfigurationListFields()
                        ))
                );
    }

    private static List<FieldDescriptor> domainIdentityConfigurationListFields() {
        return asList(
                fieldWithPath("_embedded.domainIdentityConfigurations.[].id").type("string").optional().description("Domain Identity Configuration ID"),
                fieldWithPath("_embedded.domainIdentityConfigurations.[].domainId").description("The Domain Id"),
                fieldWithPath("_embedded.domainIdentityConfigurations.[].authorizationCodeType").description("The format of authorization code generated in authorization_code grant type flow"),
                subsectionWithPath("_embedded.domainIdentityConfigurations.[].authorizationCodeTypeProperties").description("Properties for customizing the authorization code generation").optional(),
                fieldWithPath("_embedded.domainIdentityConfigurations.[].authorizationCodeTTLSeconds").description("Validity of the authorization code in seconds"),
                fieldWithPath("_embedded.domainIdentityConfigurations.[].maxAuthorizationTTLSeconds").description("Maximum validity of Granted Authorization in seconds (regardless of any authorization type)"),
                fieldWithPath("_embedded.domainIdentityConfigurations.[].refreshTokenEnabled").description("If enabled, refresh token will be issued if the flow supports it."),
                fieldWithPath("_embedded.domainIdentityConfigurations.[].accessTokenProvider").description("The provider used for generating access tokens"),
                subsectionWithPath("_embedded.domainIdentityConfigurations.[].accessTokenProperties").description("Properties for customizing the access token generation").optional(),
                fieldWithPath("_embedded.domainIdentityConfigurations.[].refreshTokenProvider").description("The provider used for generating refresh tokens"),
                subsectionWithPath("_embedded.domainIdentityConfigurations.[].refreshTokenProperties").description("Properties for customizing the refresh token generation").optional(),
                fieldWithPath("_embedded.domainIdentityConfigurations.[].issuerId").description("issuer id of the domain")
        );
    }

    @Test
    public void updateDomainIdentityConfigurationById() throws Exception {
        final Long authorizationCodeTTLSeconds = 300L;
        DomainIdentityConfiguration configuration = domainIdentityConfigurationRepository.save(newDomainIdentityConfiguration());
        mockMvc.perform(
                RestDocumentationRequestBuilders.patch(
                        "/admin/repo/domainIdentityConfigurations/{domainIdentityConfigurationId}",
                        configuration.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.identity_config.update"))
                        .content(
                                objectMapper.createObjectNode()
                                        .put("authorizationCodeTTLSeconds", authorizationCodeTTLSeconds)
                                        .toString()
                        )
        ).andExpect(status().isNoContent())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                domainIdentityConfigurationIdParameter()
                        ))
                );
        assertThat(domainIdentityConfigurationRepository.findById(configuration.getId()).get().getAuthorizationCodeTTLSeconds(),
                equalTo(authorizationCodeTTLSeconds));
    }

    @Test
    public void deleteDomainIdentityConfigurationById() throws Exception {
        DomainIdentityConfiguration configuration = domainIdentityConfigurationRepository.save(newDomainIdentityConfiguration());
        mockMvc.perform(
                RestDocumentationRequestBuilders.delete(
                        "/admin/repo/domainIdentityConfigurations/{domainIdentityConfigurationId}",
                        configuration.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.identity_config.delete"))
        ).andExpect(status().isNoContent())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                domainIdentityConfigurationIdParameter()
                        ))
                );
        assertThat(domainIdentityConfigurationRepository.existsById(configuration.getId()), equalTo(false));
    }
}
