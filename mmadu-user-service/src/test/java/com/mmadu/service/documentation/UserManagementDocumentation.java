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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.service.entities.AppUser;
import java.util.List;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class UserManagementDocumentation extends AbstractDocumentation {

    @Test
    public void createUser() throws Exception {
        AppUser user = createAppUserWithConstantId();
        mockMvc.perform(post("/appUsers")
                .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
            .content(objectToString(user)))
                .andExpect(status().isCreated())
                .andDo(document(DOCUMENTATION_NAME, requestFields(
                        fieldWithPath("domainId").description("Domain Id of the user"),
                        fieldWithPath("username").description("Username of the user"),
                        fieldWithPath("password").description("password of the user"),
                        fieldWithPath("id").optional().description("ID of the user (optional, auto-generated)"),
                        fieldWithPath("roles").type("string list").description("List of roles assigned to this user"),
                        fieldWithPath("authorities").type("string list").description("List of authorities given to ths user"),
                        subsectionWithPath("properties").optional().type("map").description("Other user properties")
                )));
    }

    @Test
    public void gettingAllUsers() throws Exception {
        List<AppUser> appUserList = createMultipleUsers(3);
        appUserRepository.saveAll(appUserList);
        mockMvc.perform(get("/appUsers")
                .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
        )
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME, usersResponseFields()));
    }

    private ResponseFieldsSnippet usersResponseFields() {
        return responseFields(
                fieldWithPath("_embedded.appUsers.[].domainId").description("Domain Id of the user"),
                fieldWithPath("_embedded.appUsers.[].username").description("Username of the user"),
                fieldWithPath("_embedded.appUsers.[].password").description("password of the user"),
                fieldWithPath("_embedded.appUsers.[].roles").type("string list").description("List of roles assigned to this user"),
                fieldWithPath("_embedded.appUsers.[].authorities").type("string list").description("List of authorities given to ths user"),
                subsectionWithPath("_embedded.appUsers.[].properties").optional().type("map").description("Other user properties"),
                subsectionWithPath("_embedded.appUsers.[]._links").type("map").description("User item resource links"),
                subsectionWithPath("_links").type("map").description("Resource links"),
                subsectionWithPath("page").type("map").description("Page information")
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
