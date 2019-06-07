package com.mmadu.service.documentation;

import static com.mmadu.service.utilities.DomainAuthenticationConstants.DOMAIN_AUTH_TOKEN_FIELD;
import static java.util.Arrays.asList;
import static org.assertj.core.util.Maps.newHashMap;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.service.entities.AppUser;
import java.util.List;

import com.mmadu.service.model.UserView;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class UserManagementDocumentation extends AbstractDocumentation {

    @Test
    public void createUser() throws Exception {
        UserView user = new UserView("user", "password",
                asList("admin"), asList("manage-users"), newHashMap("color", "blue"));
        mockMvc.perform(post("/domains/{domainId}/users", USER_DOMAIN_ID)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
            .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andDo(document(DOCUMENTATION_NAME, relaxedRequestFields(
                        fieldWithPath("username").description("The user's username (must be unigue)"),
                        fieldWithPath("id").description("The user's id (unique identifier used to reference user in your application)"),
                        fieldWithPath("password").description("The user's password"),
                        fieldWithPath("roles").description("The user's assigned roles"),
                        fieldWithPath("authorities").description("The user's granted authorities")
                ) ,pathParameters(
                        parameterWithName("domainId").description("The domain id of the user")
                )));
    }

    @Test
    public void gettingAllUsers() throws Exception {
        List<AppUser> appUserList = createMultipleUsers(3);
        appUserRepository.saveAll(appUserList);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/domains/{domainId}/users", USER_DOMAIN_ID)
                .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
        )
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME, usersResponseFields()));
    }

    private ResponseFieldsSnippet usersResponseFields() {
        return relaxedResponseFields(
                fieldWithPath("content.[].id").description("The user's unique identification"),
                fieldWithPath("content.[].username").description("Username of the user"),
                fieldWithPath("content.[].username").description("password of the user"),
                fieldWithPath("content.[].roles").type("string list").description("List of roles assigned to this user"),
                fieldWithPath("content.[].authorities").type("string list").description("List of authorities given to ths user")
        );
    }

    @Test
    public void gettingAUserById() throws Exception {
        createAUserAndSave();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/appUsers/{userId}", TEST_USER_ID)
                .header(DOMAIN_AUTH_TOKEN_FIELD, DOMAIN_TOKEN)
        )
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("userId").description("The user's ID")
                        ), userResponseFields()));
    }

    private ResponseFieldsSnippet userResponseFields() {
        return responseFields(
                fieldWithPath("domainId").description("Domain Id of the user"),
                fieldWithPath("username").description("Username of the user"),
                fieldWithPath("password").description("password of the user"),
                fieldWithPath("roles").type("string list").description("List of roles assigned to this user"),
                fieldWithPath("authorities").type("string list").description("List of authorities given to ths user"),
                subsectionWithPath("properties").optional().type("map").description("Other user properties"),
                subsectionWithPath("_links").type("map").description("User item resource links")
        );
    }

    private void createAUserAndSave() {
        AppUser user = createAppUserWithConstantId();
        appUserRepository.save(user);
    }

    @Test
    public void deletingAUserById() throws Exception {
        createAUserAndSave();
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/appUsers/{userId}", TEST_USER_ID)
                .header(DOMAIN_AUTH_TOKEN_FIELD, DOMAIN_TOKEN)
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("userId").description("The user's ID")
                        )));
    }

    @Test
    public void updatingUserProperties() throws Exception {
        createAUserAndSave();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("password", "new-password")
                .putObject("properties").put("favourite-colour", "green");
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/appUsers/{userId}", TEST_USER_ID)
                .header(DOMAIN_AUTH_TOKEN_FIELD, DOMAIN_TOKEN)
                .content(objectNode.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("userId").description("The user's ID")
                        )));
    }

    @Test
    public void gettingAUserByUsernameAndDomain() throws Exception {
        createAUserAndSave();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/appUsers/search/findByUsernameAndDomainId")
                .header(DOMAIN_AUTH_TOKEN_FIELD, DOMAIN_TOKEN)
                .param("username", USERNAME)
                .param("domainId", USER_DOMAIN_ID))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME, userResponseFields(),
                        requestParameters(
                                parameterWithName("username").description("The username of the user"),
                                parameterWithName("domainId").description("The domain id of the user")
                        )));
    }

    @Test
    public void gettingAllUsersInADomain() throws Exception {
        List<AppUser> appUserList = createMultipleUsers(3);
        appUserRepository.saveAll(appUserList);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/appUsers/search/findByDomainId")
                .header(DOMAIN_AUTH_TOKEN_FIELD, DOMAIN_TOKEN)
                .param("domainId", USER_DOMAIN_ID))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME, usersResponseFields(),
                        requestParameters(
                                parameterWithName("domainId").description("The domain id of the user")
                        )));
    }
}
