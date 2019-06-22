package com.mmadu.service.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.service.models.AuthenticateRequest;
import com.mmadu.service.models.AuthenticateResponse;
import com.mmadu.service.services.AuthenticationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.mmadu.service.models.AuthenticationStatus.AUTHENTICATED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = AuthenticationController.class, secure = false)
public class AuthenticationControllerTest {

    public static final String DOMAIN_ID = "domain-0";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService service;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testAuthentication() throws Exception {
        doReturn(AuthenticateResponse.builder().status(AUTHENTICATED).build())
                .when(service).authenticate(eq(DOMAIN_ID), any());

        mockMvc.perform(post("/domains/{domainId}/authenticate", DOMAIN_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(
                        AuthenticateRequest.builder().password("password").username("username").build())))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value(AUTHENTICATED.name()));
    }
}