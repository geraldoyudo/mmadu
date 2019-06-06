package com.mmadu.service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.service.model.UserView;
import com.mmadu.service.services.UserManagementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserManagementController.class, secure = false)
public class UserManagementControllerTest {
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
    public void givenBadUserArgment400BadRequest() throws Exception {
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
}
