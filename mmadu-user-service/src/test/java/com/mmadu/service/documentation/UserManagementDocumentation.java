package com.mmadu.service.documentation;

import com.mmadu.service.entities.AppUser;
import com.mmadu.service.model.UserView;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import java.util.List;

import static com.mmadu.service.utilities.DomainAuthenticationConstants.DOMAIN_AUTH_TOKEN_FIELD;
import static java.util.Arrays.asList;
import static org.assertj.core.util.Maps.newHashMap;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserManagementDocumentation extends AbstractDocumentation {

    @Before
    public void setUp(){
        appUserRepository.deleteAll();
    }

    @Test
    public void createUser() throws Exception {
        UserView user = new UserView("user", "password",
                asList("admin"), asList("manage-users"), newHashMap("color", "blue"));
        mockMvc.perform(post("/domains/{domainId}/users", USER_DOMAIN_ID)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(DOMAIN_AUTH_TOKEN_FIELD, DOMAIN_TOKEN)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andDo(document(DOCUMENTATION_NAME, relaxedRequestFields(
                        fieldWithPath("username").description("The user's username (must be unigue)"),
                        fieldWithPath("id").description("The user's id (unique identifier used to reference user in your application)"),
                        fieldWithPath("password").description("The user's password"),
                        fieldWithPath("roles").description("The user's assigned roles"),
                        fieldWithPath("authorities").description("The user's granted authorities")
                ), pathParameters(
                        parameterWithName("domainId").description("The domain id of the user")
                )));
    }

    @Test
    public void gettingAllUsers() throws Exception {
        List<AppUser> appUserList = createMultipleUsers(3);
        appUserRepository.saveAll(appUserList);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/domains/{domainId}/users", USER_DOMAIN_ID)
                .param("page", "0")
                .param("size", "10")
                .header(DOMAIN_AUTH_TOKEN_FIELD, DOMAIN_TOKEN)
        )
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME,
                        requestParameters(
                                parameterWithName("page").description("page number to request"),
                                parameterWithName("size").description("maximum number of items in page")
                        ),
                        usersResponseFields()));
    }

    private ResponseFieldsSnippet usersResponseFields() {
        return relaxedResponseFields(
                fieldWithPath("content.[].id").description("The user's unique identification"),
                fieldWithPath("content.[].username").description("Username of the user"),
                fieldWithPath("content.[].password").description("password of the user"),
                fieldWithPath("content.[].roles").type("string list").description("List of roles assigned to this user"),
                fieldWithPath("content.[].authorities").type("string list").description("List of authorities given to ths user")
        );
    }

    @Test
    public void gettingAUserById() throws Exception {
        createAUserAndSave();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/domains/{domainId}/users/{userId}",
                USER_DOMAIN_ID, USER_EXTERNAL_ID)
                .header(DOMAIN_AUTH_TOKEN_FIELD, DOMAIN_TOKEN)
        )
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("userId").description("The user's ID"),
                                parameterWithName("domainId").description("The domain id of the user")
                        ), userResponseFields()));
    }

    private ResponseFieldsSnippet userResponseFields() {
        return relaxedResponseFields(
                fieldWithPath("id").description("The user's id"),
                fieldWithPath("username").description("Username of the user"),
                fieldWithPath("password").description("password of the user"),
                fieldWithPath("roles").type("string list").description("List of roles assigned to this user"),
                fieldWithPath("authorities").type("string list").description("List of authorities given to ths user")
        );
    }

    private void createAUserAndSave() {
        AppUser user = createAppUserWithConstantId();
        appUserRepository.save(user);
    }

    @Test
    public void deletingAUserById() throws Exception {
        createAUserAndSave();
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/domains/{domainId}/users/{userId}",
                USER_DOMAIN_ID, USER_EXTERNAL_ID)
                .header(DOMAIN_AUTH_TOKEN_FIELD, DOMAIN_TOKEN)
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("domainId").description("The user's domain ID"),
                                parameterWithName("userId").description("The user's ID")
                        )));
    }

    @Test
    public void updatingUserProperties() throws Exception {
        createAUserAndSave();
        UserView userView = appUserRepository.findById(TEST_USER_ID).get().userView();
        userView.setUsername("changed-username");
        userView.setPassword("changed-password");
        mockMvc.perform(RestDocumentationRequestBuilders.put("/domains/{domainId}/users/{userId}",
                USER_DOMAIN_ID, USER_EXTERNAL_ID)
                .header(DOMAIN_AUTH_TOKEN_FIELD, DOMAIN_TOKEN)
                .content(objectMapper.writeValueAsString(userView))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("domainId").description("The user's domain ID"),
                                parameterWithName("userId").description("The user's ID")
                        )));
    }

    @Test
    public void gettingAUserByUsernameAndDomain() throws Exception {
        createAUserAndSave();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/domains/{domainId}/users/load", USER_DOMAIN_ID)
                .header(DOMAIN_AUTH_TOKEN_FIELD, DOMAIN_TOKEN)
                .param("username", USERNAME))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME, userResponseFields(),
                        requestParameters(
                                parameterWithName("username").description("The username of the user")
                        ),
                        pathParameters(
                                parameterWithName("domainId").description("The domain id of the user")
                        )
                ));
    }

    @Test
    public void queryingUsers() throws Exception {
        List<AppUser> appUserList = createMultipleUsers(3);
        appUserRepository.saveAll(appUserList);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/domains/{domainId}/users/search", USER_DOMAIN_ID)
                .param("page", "0")
                .param("size", "10")
                .param("query", "country equals 'Nigeria' and favorite-color equals 'blue'")
                .header(DOMAIN_AUTH_TOKEN_FIELD, DOMAIN_TOKEN)
        )
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME,
                        requestParameters(
                                parameterWithName("query").description("The query search string. " +
                                        "Use any of your custom properties for this search including username"),
                                parameterWithName("page").description("page number to request"),
                                parameterWithName("size").description("maximum number of items in page")
                        ),
                        usersResponseFields()));
    }
}
