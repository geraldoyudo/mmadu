package com.mmadu.tokenservice.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.tokenservice.entities.AppToken;
import com.mmadu.tokenservice.exceptions.TokenNotFoundException;
import com.mmadu.tokenservice.models.CheckTokenRequest;
import com.mmadu.tokenservice.services.AppTokenService;
import com.mmadu.tokenservice.services.DomainConfigurationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TokenController.class, secure = false)
public class TokenControllerTest {

    private static final String TOKEN_VALUE = "1234";
    private static final String REFRESHED_TOKEN_VALUE = "4321";
    private static final String TOKEN_ID = "1234";
    private static final String DOMAIN_ID = "1111";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppTokenService tokenService;
    @MockBean
    private DomainConfigurationService domainConfigurationService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        doThrow(new TokenNotFoundException()).when(tokenService).getToken("invalid-id");
        doThrow(new TokenNotFoundException()).when(tokenService).resetToken("invalid-id");
    }

    @Test
    public void generateToken() throws Exception {
        doReturn(newAppToken("1", TOKEN_VALUE)).when(tokenService).generateToken();
        mockMvc.perform(get("/token/generate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.value").value(notNullValue()));
    }

    private AppToken newAppToken(String id, String value) {
        AppToken token = new AppToken();
        token.setId(id);
        token.setValue(value);
        return token;
    }

    @Test
    public void getToken() throws Exception {
        saveTokenWithId("1");
        mockMvc.perform(get("/token/retrieve/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.value").value(equalTo(TOKEN_VALUE)));
    }

    @Test
    public void getTokenWithInvalidId() throws Exception {
        mockMvc.perform(get("/token/retrieve/invalid-id"))
                .andExpect(status().isNotFound());
    }

    private AppToken saveTokenWithId(String tokenId) {
        AppToken token = newAppToken(tokenId, TOKEN_VALUE);
        doReturn(token).when(tokenService).getToken(tokenId);
        return token;
    }

    @Test
    public void resetToken() throws Exception {
        AppToken token = saveTokenWithId("1");
        doReturn(newAppToken("1", REFRESHED_TOKEN_VALUE)).when(tokenService).resetToken("1");
        mockMvc.perform(get("/token/reset/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(equalTo(token.getId())))
                .andExpect(jsonPath("$.value").value(equalTo(REFRESHED_TOKEN_VALUE)));
    }

    @Test
    public void resetTokenWithInvalidId() throws Exception {
        mockMvc.perform(get("/token/reset/invalid-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void checkToken() throws Exception {

        doReturn(true).when(domainConfigurationService).tokenMatchesDomain(TOKEN_ID, DOMAIN_ID);
        CheckTokenRequest request = new CheckTokenRequest();
        request.setDomainId(DOMAIN_ID);
        request.setToken(TOKEN_ID);
        mockMvc.perform(
                post("/token/checkDomainToken")
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("matches", equalTo(true)));
    }

    @Test
    public void setDomainAuthenticationToken() throws Exception {
        mockMvc.perform(
                post("/token/setDomainAuthToken")
                        .content(
                                objectMapper.createObjectNode()
                                        .put("tokenId", TOKEN_ID)
                                        .put("domainId", DOMAIN_ID)
                                        .toString()
                        )
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isNoContent());
        verify(domainConfigurationService, times(1)).setAuthTokenForDomain(TOKEN_ID, DOMAIN_ID);
    }
}