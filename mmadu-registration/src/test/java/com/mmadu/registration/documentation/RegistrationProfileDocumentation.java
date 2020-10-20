package com.mmadu.registration.documentation;

import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.models.registration.DefaultAccountStatus;
import com.mmadu.registration.repositories.RegistrationProfileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegistrationProfileDocumentation extends AbstractDocumentation {
    @Autowired
    private RegistrationProfileRepository registrationProfileRepository;

    @AfterEach
    void tearDown() {
        registrationProfileRepository.deleteAll();
    }

    @Test
    void createRegistrationProfile() throws Exception {
        RegistrationProfile profile = createNewRegistrationProfile();
        mockMvc.perform(
                post("/repo/registrationProfiles")
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.0.reg_profile.create"))
                        .content(objectMapper.writeValueAsString(profile))
        )
                .andExpect(status().isCreated())
                .andDo(
                        document(DOCUMENTATION_NAME,
                                requestFields(
                                        registrationProfileFields()
                                )
                        )
                );
    }

    private List<FieldDescriptor> registrationProfileFields() {
        return asList(
                fieldWithPath("id").optional().type("string")
                        .description("Registration profile ID (auto generated if absent)"),
                fieldWithPath("domainId").description("domain ID"),
                fieldWithPath("defaultRoles")
                        .description("Roles assigned to newly registered users"),
                fieldWithPath("defaultAuthorities")
                        .description("Authorities assigned to newly registered users"),
                fieldWithPath("defaultGroups")
                        .description("Groups assigned to newly registered users"),
                fieldWithPath("code")
                        .description("Registration Profile code, used in the registration url"),
                fieldWithPath("fields")
                        .description("Field codes of domain fields to display in this form"),
                fieldWithPath("defaultRedirectUrl")
                        .description("Url to redirect to after registration success"),
                fieldWithPath("resourcesBaseUrl")
                        .description("Base Url to resolve html js and css dependencies (in case of serving page through proxy)"),
                fieldWithPath("formUrl").description("Url to send the POST request upon registration form submit (for proxy and custom handling)"),
                fieldWithPath("headerOne").description("Main registration page title - h1"),
                fieldWithPath("headerTwo").description("Secondary registration page title - h2"),
                fieldWithPath("headerThree").description("Tertiary registration page title - h3"),
                fieldWithPath("instruction")
                        .description("Instruction message for registering " +
                                "users displayed at the top of the form"),
                subsectionWithPath("defaultAccountStatus").description("Default account status configuration"),
                fieldWithPath("submitButtonTitle")
                        .description("Submit button text")
        );
    }

    private static RegistrationProfile createNewRegistrationProfile() {
        RegistrationProfile profile = new RegistrationProfile();
        profile.setDefaultRoles(Collections.singletonList("member"));
        profile.setDefaultAuthorities(Collections.singletonList("view-list"));
        profile.setDefaultGroups(Collections.singletonList("admins"));
        profile.setId("1");
        profile.setDefaultRedirectUrl("http://my.app.com/home");
        profile.setDomainId("0");
        profile.setHeaderOne("My App");
        profile.setHeaderTwo("Register");
        profile.setHeaderThree("Fill all required fields");
        profile.setInstruction("Ensure that all fields are filled");
        profile.setSubmitButtonTitle("Go");
        profile.setCode("user");
        profile.setFields(asList("field.username", "field.email", "field.password"));
        profile.setDefaultAccountStatus(new DefaultAccountStatus());
        return profile;
    }

    @Test
    void getRegistrationProfileById() throws Exception {
        RegistrationProfile profile = registrationProfileRepository.save(createNewRegistrationProfile());
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/repo/registrationProfiles/{profileId}",
                        profile.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.global.reg_profile.read"))
        )
                .andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME,
                                relaxedResponseFields(
                                        registrationProfileFields()
                                ),
                                pathParameters(
                                        parameterWithName("profileId").description("The registration profile ID")
                                )
                        )
                );
    }

    private List<FieldDescriptor> registrationProfileFieldList() {
        return asList(
                subsectionWithPath("_embedded.registrationProfiles").description("Registration Profile Array, (See Registartion Profile Details Response)")
        );
    }


    @Test
    void getRegistrationProfileForDomainId() throws Exception {
        RegistrationProfile profile = registrationProfileRepository.save(createNewRegistrationProfile());
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/repo/registrationProfiles/search/findByDomainId")
                        .param("domainId", profile.getDomainId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.0.reg_profile.read"))
        )
                .andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME,
                                relaxedResponseFields(
                                        registrationProfileFieldList()
                                ),
                                requestParameters(
                                        parameterWithName("domainId").description("Domain ID")
                                )
                        )
                );
    }

    @Test
    void getRegistrationProfileForDomainIdAndCode() throws Exception {
        RegistrationProfile profile = registrationProfileRepository.save(createNewRegistrationProfile());
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/repo/registrationProfiles/search/findByDomainIdAndCode")
                        .param("domainId", profile.getDomainId())
                        .param("code", profile.getCode())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.0.reg_profile.read"))
        )
                .andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME,
                                relaxedResponseFields(
                                        registrationProfileFields()
                                ),
                                requestParameters(
                                        parameterWithName("domainId").description("Domain ID"),
                                        parameterWithName("code").description("Registration Profile code")
                                )
                        )
                );
    }

    @Test
    void updateRegistrationProfileById() throws Exception {
        final String modifiedRedirectUrl = "http://modified.app.com";

        RegistrationProfile profile = registrationProfileRepository.save(createNewRegistrationProfile());
        mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/repo/registrationProfiles/{profileId}",
                        profile.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.0.reg_profile.update"))
                        .content(
                                objectMapper.createObjectNode()
                                        .put("defaultRedirectUrl", modifiedRedirectUrl)
                                        .toString()
                        )
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME, pathParameters(
                        parameterWithName("profileId").description("The registration profile ID")
                )));
        assertThat(registrationProfileRepository.findById(profile.getId()).get().getDefaultRedirectUrl(),
                equalTo(modifiedRedirectUrl));
    }

    @Test
    void deleteRegistrationProfileById() throws Exception {
        RegistrationProfile profile = registrationProfileRepository.save(createNewRegistrationProfile());
        mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/repo/registrationProfiles/{profileId}",
                        profile.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.0.reg_profile.delete"))
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME, pathParameters(
                        parameterWithName("profileId").description("The registration profile ID")
                )));
        assertThat(registrationProfileRepository.existsById(profile.getId()), equalTo(false));
    }

}
