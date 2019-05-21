package com.mmadu.service.integration;

import static com.mmadu.service.models.AuthenticationStatus.AUTHENTICATED;
import static com.mmadu.service.utilities.DomainAuthenticationConstants.DOMAIN_AUTH_TOKEN_FIELD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.service.config.MongoInitializationConfig;
import com.mmadu.service.entities.AppUser;
import com.mmadu.service.models.AuthenticateRequest;
import com.mmadu.service.repositories.AppUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(MongoInitializationConfig.class)
public class AuthenticationApiDomainTokenSecurityIT {

    private static final String TOKEN = "1234";

    @Autowired
    private WebApplicationContext context;
    protected MockMvc mockMvc;
    @Autowired
    private AppUserRepository appUserRepository;
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void givenNoDomainTokenHeaderWhenAuthenticateShouldReturnUnAuthorized() throws Exception {
        this.mockMvc.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).content(
                mapper.writeValueAsString(
                        AuthenticateRequest.builder().username("user").password("password").domain("1").build())))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenWrongDomainTokenHeaderWhenAuthenticateShouldReturnUnAuthorized() throws Exception {
        this.mockMvc.perform(
                post("/authenticate").header(DOMAIN_AUTH_TOKEN_FIELD, "33333").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(
                                AuthenticateRequest.builder().username("user").password("password").domain("1")
                                        .build()))).andExpect(status().isForbidden());
    }

    @Test
    public void givenCorrectDomainTokenHeaderWhenAuthenticateShouldReturnAuthorized() throws Exception {
        createAppUser();
        this.mockMvc.perform(
                post("/authenticate").header(DOMAIN_AUTH_TOKEN_FIELD, getToken()).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(
                                AuthenticateRequest.builder().username("user").password("password").domain("1")
                                        .build()))).andExpect(status().isOk())
                                        .andExpect(jsonPath("$.status").value(AUTHENTICATED.name()));
    }

    private void createAppUser() {
        AppUser user = new AppUser();
        user.setUsername("user");
        user.setPassword("password");
        user.setDomainId("1");
        appUserRepository.save(user);
    }

    protected String getToken(){
        return TOKEN;
    }
}
