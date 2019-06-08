package com.mmadu.service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.service.exceptions.DuplicationException;
import com.mmadu.service.exceptions.UserNotFoundException;
import com.mmadu.service.model.UserView;
import com.mmadu.service.services.UserManagementService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserManagementController.class, secure = false)
public class UserManagementControllerTest {
    public static final String USER_ID = "13423";
    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    public static final String BAD_ARGUMENT_CODE = "215";
    public static final String FIELD_ERROR_CODE = "code";
    public static final String FIELD_ERROR_MESSAGE = "message";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserManagementService userManagementService;

    private static final String DOMAIN_ID = "1234";

    @Test
    public void createUser() throws Exception {
        mockMvc.perform(
                post("/domains/{domainId}/users", DOMAIN_ID)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(testUser())
        ).andExpect(status().isCreated());
    }

    private String testUser() {
        ObjectNode user = objectMapper.createObjectNode();
        user.put("username", "test-user")
                .put("password", "password")
                .putArray("roles").add("admin");
        user.putArray("authorities").add("manage-users");
        user.put("email", "user@email")
                .put("nationality", "Nigerian");
        return user.toString();
    }

    @Test
    public void givenBadArgmentWhenCreateUserReturn400BadRequest() throws Exception {
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
    public void givenDuplicateWhenCreateWhenCreateUserReturnDuplicateException() throws Exception {
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
    public void givenUserListWhenGetAllUsersReturnUserPage() throws Exception {
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
        collector.checkThat(p.getPageNumber(), equalTo(2));
        collector.checkThat(p.getPageSize(), equalTo(10));
    }

    private List<UserView> createUserViewList(){
        List<UserView> userViewList = new LinkedList<>();
        for(int i=0; i< 5; ++i){
            UserView userView = new UserView(
                    "id" + i,
                    "user"+i,
                    "password" +i,
                    asList("member"),
                    asList("view-profile"),
                    new HashMap<>()
            );
            userViewList.add(userView);
        }
        return userViewList;
    }

    @Test
    public void givenNoUserWhenGetUserByExternalIdThenReturn404Response() throws Exception {
        doThrow(new UserNotFoundException())
                .when(userManagementService).getUserByDomainIdAndExternalId(DOMAIN_ID, USER_ID);
        mockMvc.perform(
                get("/domains/{domainId}/users/{userId}", DOMAIN_ID, USER_ID)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenUserWhenGetUserByExternalIdThenReturnUser() throws Exception {
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
                .andExpect(jsonPath("roles" , equalTo(userView.getRoles())))
                .andExpect(jsonPath("authorities" , equalTo(userView.getAuthorities())));
    }

    @Test
    public void givenNoUserWhenDeleteAUserByDomainAndExternalIdThenReturn404NotFound() throws Exception{
        doThrow(new UserNotFoundException()).when(userManagementService)
                .deleteUserByDomainAndExternalId(DOMAIN_ID, USER_ID);
        mockMvc.perform(
                delete("/domains/{domainId}/users/{userId}", DOMAIN_ID, USER_ID)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenUserWhenDeleteAUserByDomainAndExternalIdThenReturn404NotFound() throws Exception{
        mockMvc.perform(
                delete("/domains/{domainId}/users/{userId}", DOMAIN_ID, USER_ID)
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenUserWhenUpdateAllUserPropertiesThenReturnNoContent() throws Exception {
        ArgumentCaptor<UserView> userCaptor = ArgumentCaptor.forClass(UserView.class);
        doNothing().when(userManagementService).updateUser(eq(DOMAIN_ID), eq(USER_ID), userCaptor.capture());
        mockMvc.perform(
                put("/domains/{domainId}/users/{userId}", DOMAIN_ID, USER_ID)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(testUser())
        )
                .andExpect(status().isNoContent());
        UserView userView = userCaptor.getValue();
        collector.checkThat(userView.getUsername(), equalTo("test-user"));
        collector.checkThat(userView.getProperty("nationality").orElse(""), equalTo("Nigerian"));
        collector.checkThat(userView.getRoles(), equalTo(asList("admin")));
        collector.checkThat(userView.getAuthorities(), equalTo(asList("manage-users")));
    }
}
