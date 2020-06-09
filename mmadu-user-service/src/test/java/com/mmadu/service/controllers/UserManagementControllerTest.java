package com.mmadu.service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.service.exceptions.DuplicationException;
import com.mmadu.service.exceptions.UserNotFoundException;
import com.mmadu.service.models.PatchOperation;
import com.mmadu.service.models.UpdateRequest;
import com.mmadu.service.models.UserPatch;
import com.mmadu.service.models.UserView;
import com.mmadu.service.services.AuthorityManagementService;
import com.mmadu.service.services.GroupService;
import com.mmadu.service.services.RoleManagementService;
import com.mmadu.service.services.UserManagementService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserManagementController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        })
class UserManagementControllerTest {
    public static final String USER_ID = "13423";
    public static final String USERNAME = "test-user";
    public static final String BAD_ARGUMENT_CODE = "215";
    public static final String FIELD_ERROR_CODE = "code";
    public static final String FIELD_ERROR_MESSAGE = "message";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserManagementService userManagementService;
    @MockBean
    private GroupService groupService;
    @MockBean
    private RoleManagementService roleManagementService;
    @MockBean
    private AuthorityManagementService authorityManagementService;

    private static final String DOMAIN_ID = "1234";

    @Test
    void createUser() throws Exception {
        mockMvc.perform(
                post("/domains/{domainId}/users", DOMAIN_ID)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(testUser())
        ).andExpect(status().isCreated());
    }

    private String testUser() {
        ObjectNode user = objectMapper.createObjectNode();
        user.put("username", USERNAME)
                .put("password", "password");
        user.put("email", "user@email")
                .put("nationality", "Nigerian");
        return user.toString();
    }

    @Test
    void givenBadArgmentWhenCreateUserReturn400BadRequest() throws Exception {
        String errorMessage = "invalid user";
        doThrow(new IllegalArgumentException(errorMessage)).when(userManagementService)
                .createUser(anyString(), any(UserView.class));

        mockMvc.perform(post("/domains/{domainId}/users", DOMAIN_ID)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.createObjectNode()
                        .put("password", "password").toString()
                ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(FIELD_ERROR_CODE, equalTo(BAD_ARGUMENT_CODE)))
                .andExpect(jsonPath(FIELD_ERROR_MESSAGE, equalTo(errorMessage)));
    }

    @Test
    void givenDuplicateWhenCreateWhenCreateUserReturnDuplicateException() throws Exception {
        String errorMessage = "user already exists";
        doThrow(new DuplicationException(errorMessage)).when(userManagementService)
                .createUser(anyString(), any(UserView.class));

        mockMvc.perform(post("/domains/{domainId}/users", DOMAIN_ID)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(testUser()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(FIELD_ERROR_CODE, equalTo("220")))
                .andExpect(jsonPath(FIELD_ERROR_MESSAGE, equalTo(errorMessage)));
    }

    @Test
    void givenUserListWhenGetAllUsersReturnUserPage() throws Exception {
        PageImpl<UserView> page = new PageImpl<>(createUserViewList());
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        doReturn(page).when(userManagementService).getAllUsers(eq(DOMAIN_ID), pageableCaptor.capture());
        mockMvc.perform(get("/domains/{domainId}/users", DOMAIN_ID)
                .param("page", "2")
                .param("size", "10")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("content.length()", equalTo(5)))
                .andExpect(jsonPath("totalPages", equalTo(1)))
                .andExpect(jsonPath("totalElements", equalTo(5)));
        Pageable p = pageableCaptor.getValue();
        assertAll(
                () -> assertThat(p.getPageNumber(), equalTo(2)),
                () -> assertThat(p.getPageSize(), equalTo(10))
        );
    }

    private List<UserView> createUserViewList() {
        List<UserView> userViewList = new LinkedList<>();
        for (int i = 0; i < 5; ++i) {
            UserView userView = new UserView(
                    "id" + i,
                    "user" + i,
                    "password" + i,
                    asList("member"),
                    asList("view-profile"),
                    new HashMap<>()
            );
            userViewList.add(userView);
        }
        return userViewList;
    }

    @Test
    void givenNoUserWhenGetUserByExternalIdThenReturn404Response() throws Exception {
        doThrow(new UserNotFoundException())
                .when(userManagementService).getUserByDomainIdAndExternalId(DOMAIN_ID, USER_ID);
        mockMvc.perform(
                get("/domains/{domainId}/users/{userId}", DOMAIN_ID, USER_ID)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUserWhenGetUserByExternalIdThenReturnUser() throws Exception {
        UserView userView = new UserView(USER_ID, "user", "password",
                asList("admin"), asList("manage-users"), new HashMap<>());
        doReturn(userView).when(userManagementService).getUserByDomainIdAndExternalId(DOMAIN_ID, USER_ID);
        mockMvc.perform(
                get("/domains/{domainId}/users/{userId}", DOMAIN_ID, USER_ID)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", equalTo(userView.getId())))
                .andExpect(jsonPath("username", equalTo(userView.getUsername())))
                .andExpect(jsonPath("password", equalTo(userView.getPassword())))
                .andExpect(jsonPath("roles", equalTo(userView.getRoles())))
                .andExpect(jsonPath("authorities", equalTo(userView.getAuthorities())));
    }

    @Test
    void givenNoUserWhenDeleteAUserByDomainAndExternalIdThenReturn404NotFound() throws Exception {
        doThrow(new UserNotFoundException()).when(userManagementService)
                .deleteUserByDomainAndExternalId(DOMAIN_ID, USER_ID);
        mockMvc.perform(
                delete("/domains/{domainId}/users/{userId}", DOMAIN_ID, USER_ID)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUserWhenDeleteAUserByDomainAndExternalIdThenReturn404NotFound() throws Exception {
        mockMvc.perform(
                delete("/domains/{domainId}/users/{userId}", DOMAIN_ID, USER_ID)
        )
                .andExpect(status().isNoContent());
    }

    @Test
    void givenUserWhenUpdateAllUserPropertiesThenReturnNoContent() throws Exception {
        ArgumentCaptor<UserView> userCaptor = ArgumentCaptor.forClass(UserView.class);
        doNothing().when(userManagementService).updateUser(eq(DOMAIN_ID), eq(USER_ID), userCaptor.capture());
        mockMvc.perform(
                put("/domains/{domainId}/users/{userId}", DOMAIN_ID, USER_ID)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(testUser())
        )
                .andExpect(status().isNoContent());
        UserView userView = userCaptor.getValue();
        assertAll(
                () -> assertThat(userView.getUsername(), equalTo("test-user")),
                () -> assertThat(userView.getProperty("nationality").orElse(""), equalTo("Nigerian"))
        );
    }

    @Test
    void givenNoUserWhenLoadUserByUsernameThenReturn404Response() throws Exception {
        doThrow(new UserNotFoundException())
                .when(userManagementService).getUserByDomainIdAndUsername(DOMAIN_ID, USERNAME);
        mockMvc.perform(
                get("/domains/{domainId}/users/load", DOMAIN_ID)
                        .param("username", USERNAME)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUserWhenLoadUserByUsernameThenReturnUser() throws Exception {
        UserView userView = new UserView(USER_ID, "user", "password",
                asList("admin"), asList("manage-users"), new HashMap<>());
        userView.setProperty("birthday", LocalDate.of(1993, 1, 1));
        doReturn(userView).when(userManagementService).getUserByDomainIdAndUsername(DOMAIN_ID, USERNAME);
        mockMvc.perform(
                get("/domains/{domainId}/users/load", DOMAIN_ID)
                        .param("username", USERNAME)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", equalTo(userView.getId())))
                .andExpect(jsonPath("username", equalTo(userView.getUsername())))
                .andExpect(jsonPath("password", equalTo(userView.getPassword())))
                .andExpect(jsonPath("birthday", equalTo("1993-01-01")))
                .andExpect(jsonPath("roles", equalTo(userView.getRoles())))
                .andExpect(jsonPath("authorities", equalTo(userView.getAuthorities())));
    }

    @Test
    void givenUserListWhenQueryUsersReturnUserPage() throws Exception {
        PageImpl<UserView> page = new PageImpl<>(createUserViewList());
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        String query = "color equals 'red'";
        doReturn(page).when(userManagementService).queryUsers(eq(DOMAIN_ID), eq(query),
                pageableCaptor.capture());
        mockMvc.perform(get("/domains/{domainId}/users/search", DOMAIN_ID)
                .param("page", "2")
                .param("size", "10")
                .param("query", query)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("content.length()", equalTo(5)))
                .andExpect(jsonPath("totalPages", equalTo(1)))
                .andExpect(jsonPath("totalElements", equalTo(5)));
        Pageable p = pageableCaptor.getValue();
        assertAll(
                () -> assertThat(p.getPageNumber(), equalTo(2)),
                () -> assertThat(p.getPageSize(), equalTo(10))
        );
    }

    @Test
    void givenUpdateRequestAndQueryWhenPatchUsersThenReturnNoContent() throws Exception {
        String query = "color equals 'red'";
        ArgumentCaptor<UpdateRequest> updateRequestArgumentCaptor = ArgumentCaptor.forClass(UpdateRequest.class);
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(userManagementService).patchUpdateUsers(eq(DOMAIN_ID), queryCaptor.capture(),
                updateRequestArgumentCaptor.capture());
        mockMvc.perform(patch("/domains/{domainId}/users", DOMAIN_ID)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(updateRequest(query)
                ))
                .andExpect(status().isNoContent());
        UpdateRequest updateRequest = updateRequestArgumentCaptor.getValue();
        String actualQuery = queryCaptor.getValue();
        assertAll(
                () -> assertThat(actualQuery, equalTo(query)),
                () -> assertThat(updateRequest.getUpdates(),
                        equalTo(asList(new UserPatch(PatchOperation.SET, "color", "green"))))
        );
    }

    private String updateRequest(String query) {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode
                .put("query", query)
                .putArray("updates")
                .addObject()
                .put("operation", "SET")
                .put("property", "color")
                .put("value", "green");
        return jsonNode.toString();
    }
}
