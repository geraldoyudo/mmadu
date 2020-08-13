package com.mmadu.service.documentation;

import com.mmadu.service.entities.AppUser;
import com.mmadu.service.models.PatchOperation;
import com.mmadu.service.models.UserPatch;
import com.mmadu.service.models.UserUpdateRequest;
import com.mmadu.service.models.UserView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.util.Maps.newHashMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserManagementDocumentation extends AbstractDocumentation {

    @BeforeEach
    void setUp() {
        appUserRepository.deleteAll();
    }

    @Test
    void createUser() throws Exception {
        UserView user = new UserView("user", "password", newHashMap("color", "blue"));
        user.setId("123");
        mockMvc.perform(post("/domains/{domainId}/users", USER_DOMAIN_ID)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, authorization("a.test-app.user.create"))
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andDo(document(DOCUMENTATION_NAME, relaxedRequestFields(
                        fieldWithPath("username").description("The user's username (must be unique)"),
                        fieldWithPath("id")
                                .description("The user's id " +
                                        "(unique identifier used to reference user in your application)"),
                        fieldWithPath("password").description("The user's password")
                ), pathParameters(
                        parameterWithName("domainId").description("The domain id of the user")
                )));
    }

    @Test
    void gettingAllUsers() throws Exception {
        List<AppUser> appUserList = createMultipleUsers(3);
        appUserRepository.saveAll(appUserList);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/domains/{domainId}/users", USER_DOMAIN_ID)
                .param("page", "0")
                .param("size", "10")
                .header(HttpHeaders.AUTHORIZATION, authorization("a.test-app.user.read"))
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
                fieldWithPath("content.[].roles").type("string list")
                        .description("List of roles assigned to this user"),
                fieldWithPath("content.[].authorities").type("string list")
                        .description("List of authorities given to ths user"),
                fieldWithPath("content.[].propertyValidationState")
                        .description("Property Validation state map")
        );
    }

    @Test
    void gettingAUserById() throws Exception {
        createAUserAndSave();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/domains/{domainId}/users/{userId}",
                USER_DOMAIN_ID, USER_EXTERNAL_ID)
                .header(HttpHeaders.AUTHORIZATION, authorization("a.test-app.user.read"))
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
                fieldWithPath("authorities").type("string list").description("List of authorities given to ths user"),
                fieldWithPath("propertyValidationState").description("Property validation state map")
        );
    }

    private void createAUserAndSave() {
        AppUser user = createAppUserWithConstantId();
        appUserRepository.save(user);
    }

    @Test
    void deletingAUserById() throws Exception {
        createAUserAndSave();
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/domains/{domainId}/users/{userId}",
                USER_DOMAIN_ID, USER_EXTERNAL_ID)
                .header(HttpHeaders.AUTHORIZATION, authorization("a.test-app.user.delete"))
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("domainId").description("The user's domain ID"),
                                parameterWithName("userId").description("The user's ID")
                        )));
    }

    @Test
    void updatingUserProperties() throws Exception {
        createAUserAndSave();
        mockMvc.perform(RestDocumentationRequestBuilders.put("/domains/{domainId}/users/{userId}",
                USER_DOMAIN_ID, USER_EXTERNAL_ID)
                .header(HttpHeaders.AUTHORIZATION, authorization("a.test-app.user.update"))
                .content(objectMapper.createObjectNode()
                        .put("username", "changed-username")
                        .put("password", "changed-password")
                        .toPrettyString()
                )
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("domainId").description("The user's domain ID"),
                                parameterWithName("userId").description("The user's ID")
                        )));
    }

    @Test
    void gettingAUserByUsernameAndDomain() throws Exception {
        createAUserAndSave();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/domains/{domainId}/users/load",
                USER_DOMAIN_ID)
                .header(HttpHeaders.AUTHORIZATION, authorization("a.test-app.user.load"))
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
    void queryingUsers() throws Exception {
        List<AppUser> appUserList = createMultipleUsers(3);
        appUserRepository.saveAll(appUserList);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/domains/{domainId}/users/search",
                USER_DOMAIN_ID)
                .param("page", "0")
                .param("size", "10")
                .param("query", "(country equals 'Nigeria') and (favourite-color equals 'blue')")
                .header(HttpHeaders.AUTHORIZATION, authorization("a.test-app.user.read"))
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

    @Test
    void updatingUsersByQuery() throws Exception {
        List<AppUser> appUserList = createMultipleUsers(3);
        UserUpdateRequest request = new UserUpdateRequest();
        request.setQuery("(country equals 'Nigeria')");
        request.setUpdates(asList(new UserPatch(PatchOperation.SET, "color", "green")));
        appUserRepository.saveAll(appUserList);
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/domains/{domainId}/users", USER_DOMAIN_ID)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, authorization("a.test-app.user.update"))
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME, requestFields(
                        fieldWithPath("query").description("The query criteria for updating users"),
                        fieldWithPath("updates.[].operation").description("The kind of update operation to make: " +
                                "(SET, INCREMENT, ADD, REMOVE"),
                        fieldWithPath("updates.[].property").description("The property to update"),
                        fieldWithPath("updates.[].value").description("The value used by the update operation")
                ), pathParameters(
                        parameterWithName("domainId").description("The domain id of the user")
                )));
        assertThat(appUserRepository.queryForUsers("color equals 'green'",
                PageRequest.of(0, 10)).getTotalElements(),
                equalTo(3L));
    }

    @Test
    void resetUserPassword() throws Exception {
        createAUserAndSave();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/domains/{domainId}/users/{userId}/resetPassword",
                USER_DOMAIN_ID, USER_EXTERNAL_ID)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, authorization("a.test-app.user.reset_password"))
                .content(objectMapper.createObjectNode()
                        .put("newPassword", "new-password")
                        .toPrettyString()
                )
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME, requestFields(
                        fieldWithPath("newPassword").description("The new password for the user")
                ), pathParameters(
                        parameterWithName("domainId").description("The domain id of the user"),
                        parameterWithName("userId").description("The external user id of the user")
                )));
    }

    @Test
    void setPropertyValidationState() throws Exception {
        createAUserAndSave();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/domains/{domainId}/users/{userId}/setPropertyValidationState",
                USER_DOMAIN_ID, USER_EXTERNAL_ID)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, authorization("a.test-app.user.set_property_validation_state"))
                .content(objectMapper.createObjectNode()
                        .put("propertyName", "email")
                        .put("valid", true)
                        .toPrettyString()
                )
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME, requestFields(
                        fieldWithPath("propertyName").description("The property to be validated"),
                        fieldWithPath("valid").description("Validity of the property")
                ), pathParameters(
                        parameterWithName("domainId").description("The domain id of the user"),
                        parameterWithName("userId").description("The external user id of the user")
                )));
    }
}
