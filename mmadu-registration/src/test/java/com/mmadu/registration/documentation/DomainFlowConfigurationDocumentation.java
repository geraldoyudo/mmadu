package com.mmadu.registration.documentation;

import com.mmadu.registration.entities.DomainFlowConfiguration;
import com.mmadu.registration.providers.RegistrationProfileFormFieldsManager;
import com.mmadu.registration.repositories.DomainFlowConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.ParameterDescriptor;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DomainFlowConfigurationDocumentation extends AbstractDocumentation {
    @Autowired
    private DomainFlowConfigurationRepository domainFlowConfigurationRepository;

    @Test
    void createDomainFlowConfigurations() throws Exception {
        DomainFlowConfiguration domainFlowConfiguration = createNewDomainFlowConfiguration();
        mockMvc.perform(
                post("/repo/domainFlowConfigurations")
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.global.flow_config.create"))
                        .content(objectMapper.writeValueAsString(domainFlowConfiguration))
        ).andExpect(status().isCreated())
                .andDo(
                        document(DOCUMENTATION_NAME, requestFields(
                                domainFlowConfigurationFields()
                        ))
                );
    }

    private static List<FieldDescriptor> domainFlowConfigurationFields() {
        return asList(
                fieldWithPath("id").type("string").optional().description("Domain Flow Configuration ID"),
                fieldWithPath("jwkSetUri").type("string").optional().description("The jwk set uri required for accessing api with domain token"),
                fieldWithPath("domainId").type("string").optional().description("Domain Id"),
                subsectionWithPath("passwordReset").optional().description("Password reset Configuration"),
                subsectionWithPath("passwordReset.userFields").optional().description("The user fields to use to search for a user account to reset"),
                subsectionWithPath("passwordReset.initiationFormTitle").optional().description("The title displayed on the account look up page"),
                subsectionWithPath("passwordReset.initiationFormDescription").optional().description("A description message displayed on the account look up page"),
                subsectionWithPath("passwordReset.confirmationFormTitle").optional().description("A title displayed on the password reset confirmation page"),
                subsectionWithPath("passwordReset.confirmationFormDescription").optional().description("A description message displayed on the password reset confirmation page"),
                subsectionWithPath("passwordReset.userFieldPlaceholder").optional().description("Placeholder for the account lookup field"),
                subsectionWithPath("passwordReset.submitButtonLabel").optional().description("Submit button label"),
                subsectionWithPath("passwordReset.resourcesBaseUrl").optional().description("Base Url for resolving css, js files for the form (if page is served through proxy)"),
                subsectionWithPath("passwordReset.initiationFormUrl").optional().description("POST url to send the password reset initiation request (for proxy use)"),
                subsectionWithPath("passwordReset.confirmationFormUrl").optional().description("POST url to send the password reset confirmation request (for proxy use)"),
                subsectionWithPath("passwordReset.initiationSuccessMessage").optional().description("Success message displayed to the user on successful account look up and password reset initiation"),
                subsectionWithPath("passwordReset.confirmationSuccessMessage").optional().description("Success message displayed to the user on successful password reset"),
                subsectionWithPath("passwordReset.linkPasswordConfirmationBaseUrl").optional().description("Base url used for generating passoword reset links"),
                subsectionWithPath("passwordReset.linkPasswordConfirmationUrl").optional().description("Url used for generating password reset links (for proxying requests)"),
                subsectionWithPath("passwordReset.otpProfile").optional().description("The OTP Profile used for generating password reset tokens"),
                subsectionWithPath("propertyValidation").optional().description("User property validation configuration"),
                subsectionWithPath("theme").optional().description("Theme Configuration"),
                subsectionWithPath("theme.logoSvg").optional().description("SVG code for your application logo (should be covered in <svg></svg> markup)"),
                subsectionWithPath("theme.themeColour").optional().description("CSS color for customizing user interface elements")

        );
    }

    private DomainFlowConfiguration createNewDomainFlowConfiguration() {
        DomainFlowConfiguration domainFlowConfiguration = new DomainFlowConfiguration();
        domainFlowConfiguration.setId("1");
        domainFlowConfiguration.setDomainId("1234");
        return domainFlowConfiguration;
    }

    @Test
    void getDomainFlowConfigurationById() throws Exception {
        DomainFlowConfiguration domainFlowConfiguration = domainFlowConfigurationRepository.save(createNewDomainFlowConfiguration());
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/repo/domainFlowConfigurations/{domainFlowConfigurationId}",
                        domainFlowConfiguration.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.global.flow_config.read"))
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                domainFlowConfigurationIdParameters()
                        ), relaxedResponseFields(
                                domainFlowConfigurationFields()
                        ))
                );
    }

    private ParameterDescriptor domainFlowConfigurationIdParameters() {
        return parameterWithName("domainFlowConfigurationId").description("The Domain Flow Configuration ID");
    }

    @Test
    void getAllDomainFlowConfigurations() throws Exception {
        domainFlowConfigurationRepository.save(createNewDomainFlowConfiguration());
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/repo/domainFlowConfigurations")
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.global.flow_config.read"))
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, relaxedResponseFields(
                                domainFlowConfigurationListFields()
                        ))
                );
    }

    private static List<FieldDescriptor> domainFlowConfigurationListFields() {
        return asList(
                fieldWithPath("_embedded.domainFlowConfigurations").description("Domain Flow Configuration array")
        );
    }

    @Test
    void updateDomainFlowConfigurationById() throws Exception {
        final String modifiedDomainId = "my-app-2";
        DomainFlowConfiguration domainFlowConfiguration = domainFlowConfigurationRepository.save(createNewDomainFlowConfiguration());
        mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/repo/domainFlowConfigurations/{domainFlowConfigurationId}", domainFlowConfiguration.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.global.flow_config.update"))
                        .content(
                                objectMapper.createObjectNode()
                                        .put("domainId", modifiedDomainId)
                                        .toString()
                        )
        ).andExpect(status().isNoContent())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                domainFlowConfigurationIdParameters()
                        ))
                );
        assertThat(domainFlowConfigurationRepository.findById(domainFlowConfiguration.getId()).get().getDomainId(), equalTo(modifiedDomainId));
    }

    @Test
    void deleteDomainFlowConfigurationById() throws Exception {
        DomainFlowConfiguration domainFlowConfiguration = domainFlowConfigurationRepository.save(createNewDomainFlowConfiguration());
        mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/repo/domainFlowConfigurations/{domainFlowConfigurationId}", domainFlowConfiguration.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.global.flow_config.delete"))
        ).andExpect(status().isNoContent())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                domainFlowConfigurationIdParameters()
                        ))
                );
        assertThat(domainFlowConfigurationRepository.existsById(domainFlowConfiguration.getId()), equalTo(false));
    }
}
