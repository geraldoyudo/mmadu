package com.mmadu.service.documentation;

import com.mmadu.service.entities.AppUser;
import com.mmadu.service.entities.Group;
import com.mmadu.service.entities.UserGroup;
import com.mmadu.service.models.NewGroupRequest;
import com.mmadu.service.repositories.GroupRepository;
import com.mmadu.service.repositories.UserGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import java.util.List;

import static com.mmadu.service.utilities.DomainAuthenticationConstants.DOMAIN_AUTH_TOKEN_FIELD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GroupManagementDocumentation extends AbstractDocumentation {
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private GroupRepository groupRepository;

    private Group group;

    @BeforeEach
    void setUp() {
        appUserRepository.deleteAll();
        userGroupRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @Test
    void createGroup() throws Exception {
        createAndSaveGroup();
        NewGroupRequest request = new NewGroupRequest();
        request.setName("Test Group");
        request.setIdentifier("test");
        request.setDescription("A test Group");
        request.setParentGroup(group.getIdentifier());

        mockMvc.perform(post("/domains/{domainId}/groups", USER_DOMAIN_ID)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(DOMAIN_AUTH_TOKEN_FIELD, DOMAIN_TOKEN)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(document(DOCUMENTATION_NAME, relaxedRequestFields(
                        fieldWithPath("identifier").description("The group identifier"),
                        fieldWithPath("name")
                                .description("The name of the group"),
                        fieldWithPath("description").description("A brief description of the group"),
                        fieldWithPath("parentGroup").description("The parent group of the group (if a sub group is being created)")
                ), pathParameters(
                        parameterWithName("domainId").description("The domain id of the user")
                )));
    }

    private void createAndSaveGroup() {
        group = new Group();
        group.setDomainId(USER_DOMAIN_ID);
        group.setIdentifier("sample");
        group.setDescription("Sample Group");
        group.setName("Sample Group");
        group = groupRepository.save(group);
    }

    @Test
    void addUserToGroup() throws Exception {
        createAUserAndSave();
        createAndSaveGroup();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/domains/{domainId}/groups/{groupIdentifier}/users/{userId}",
                USER_DOMAIN_ID, group.getIdentifier(), USER_EXTERNAL_ID)
                .header(DOMAIN_AUTH_TOKEN_FIELD, DOMAIN_TOKEN)
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("domainId").description("The domain id of the group"),
                                parameterWithName("groupIdentifier").description("The group identifier"),
                                parameterWithName("userId").description("The id of the user")
                        )));
    }

    private ResponseFieldsSnippet usersResponseFields() {
        return relaxedResponseFields(
                fieldWithPath("content.[].id").description("The user's unique identification"),
                fieldWithPath("content.[].username").description("Username of the user"),
                fieldWithPath("content.[].password").description("password of the user"),
                fieldWithPath("content.[].roles").type("string list")
                        .description("List of roles assigned to this user"),
                fieldWithPath("content.[].authorities").type("string list")
                        .description("List of authorities given to ths user")
        );
    }

    @Test
    void removeUserFromGroup() throws Exception {
        createAUserAndSave();
        createAndSaveGroup();
        setUserGroup();
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/domains/{domainId}/groups/{groupIdentifier}/users/{userId}",
                USER_DOMAIN_ID, group.getIdentifier(), USER_EXTERNAL_ID)
                .header(DOMAIN_AUTH_TOKEN_FIELD, DOMAIN_TOKEN)
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("domainId").description("The domain id of the group"),
                                parameterWithName("groupIdentifier").description("The group identifier"),
                                parameterWithName("userId").description("The id of the user")
                        )));
        assertEquals(0, userGroupRepository.findAll().size());
    }

    private void setUserGroup() {
        UserGroup userGroup = new UserGroup();
        userGroup.setUser(appUserRepository.findById(TEST_USER_ID).get());
        userGroup.setGroup(group);
        userGroup.setDomainId(USER_DOMAIN_ID);
        userGroupRepository.save(userGroup);
    }

    private void createAUserAndSave() {
        AppUser user = createAppUserWithConstantId();
        appUserRepository.save(user);
    }

    @Test
    void gettingAllUsersInAGroup() throws Exception {
        List<AppUser> appUserList = createMultipleUsers(3);
        appUserRepository.saveAll(appUserList);
        createAndSaveGroup();
        setToGroup(appUserList);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/domains/{domainId}/groups/{groupIdentifier}/users",
                USER_DOMAIN_ID, group.getIdentifier())
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
                        pathParameters(
                                parameterWithName("domainId").description("The domain id of the group"),
                                parameterWithName("groupIdentifier").description("The group identifier")
                        ),
                        usersResponseFields()));
    }

    private void setToGroup(List<AppUser> appUserList) {
        appUserList
                .stream()
                .map(user -> {
                    UserGroup userGroup = new UserGroup();
                    userGroup.setGroup(group);
                    userGroup.setUser(user);
                    userGroup.setDomainId(USER_DOMAIN_ID);
                    return userGroup;
                })
                .forEach(userGroupRepository::save);
    }
}
