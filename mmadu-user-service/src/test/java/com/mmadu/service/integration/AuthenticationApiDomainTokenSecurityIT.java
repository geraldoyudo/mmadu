package com.mmadu.service.integration;

import static com.mmadu.service.models.AuthenticationStatus.AUTHENTICATED;
import static com.mmadu.service.utils.DomainAuthenticationConstants.DOMAIN_AUTH_TOKEN_FIELD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.service.entities.AppToken;
import com.mmadu.service.entities.AppUser;
import com.mmadu.service.models.AuthenticateRequest;
import com.mmadu.service.repositories.AppTokenRepository;
import com.mmadu.service.repositories.AppUserRepository;
import com.mmadu.service.service.DomainPopulator;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "mmadu.domain.authenticate-api-security-enabled=true")
public class AuthenticationApiDomainTokenSecurityIT {

    private static final String TOKEN =
            "70b420bb3851d9d47acb933dbe70399bf6c92da33af01d4fb770e98c0325f41d3ebaf8986da712c82bcd4d554bf0b54023c29b624de9ef9c2f931efc580f9afb081b12e107b1e805f2b4f5f0f1d00c2d0f62634670921c505867ff20f6a8335e98af8725385586b41feff205b4e05a000823f78b5f8f5c02439ce8f67a781d90";

    @Autowired
    private WebApplicationContext context;
    protected MockMvc mockMvc;
    @Autowired
    private List<MongoRepository<?, String>> repositories;
    @Autowired
    private DomainPopulator domainPopulator;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppTokenRepository appTokenRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        repositories.forEach(MongoRepository::deleteAll);
        domainPopulator.setUpDomains();
        AppToken token = new AppToken();
        token.setValue(TOKEN);
        token.setId("1234");
        appTokenRepository.save(token);
    }

    @Test
    public void givenNoDomainTokenHeaderWhenAuthenticateShouldReturnUnAuthorized() throws Exception {
        this.mockMvc.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).content(
                mapper.writeValueAsString(
                        AuthenticateRequest.builder().username("user").password("password").domain("1").build())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenWrongDomainTokenHeaderWhenAuthenticateShouldReturnUnAuthorized() throws Exception {
        this.mockMvc.perform(
                post("/authenticate").header(DOMAIN_AUTH_TOKEN_FIELD, "1234").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(
                                AuthenticateRequest.builder().username("user").password("password").domain("1")
                                        .build()))).andExpect(status().isUnauthorized());
    }

    @Test
    public void givenCorrectDomainTokenHeaderWhenAuthenticateShouldReturnAuthorized() throws Exception {
        createAppUser();
        this.mockMvc.perform(
                post("/authenticate").header(DOMAIN_AUTH_TOKEN_FIELD, TOKEN).contentType(MediaType.APPLICATION_JSON)
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
}
