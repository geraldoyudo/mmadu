package com.mmadu.service.integration;

import static com.mmadu.service.models.AuthenticationStatus.AUTHENTICATED;
import static com.mmadu.service.utilities.DomainAuthenticationConstants.DOMAIN_AUTH_TOKEN_FIELD;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.security.DomainTokenChecker;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
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
@TestPropertySource(properties = "mmadu.domain.api-security-enabled=true")
public class AuthenticationApiDomainTokenSecurityIT {

    private static final String TOKEN = "1234";
    public static final String DOMAIN_ID = "1";

    @Autowired
    private WebApplicationContext context;
    protected MockMvc mockMvc;
    @Autowired
    private AppUserRepository appUserRepository;
    private ObjectMapper mapper = new ObjectMapper();
    @MockBean
    private DomainTokenChecker domainTokenChecker;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void givenNoDomainTokenHeaderWhenAuthenticateShouldReturnUnAuthorized() throws Exception {
        this.mockMvc.perform(post("/domains/{domainId}/authenticate", DOMAIN_ID)
                .contentType(MediaType.APPLICATION_JSON).content(
                mapper.writeValueAsString(
                       new AuthenticateRequest("user", "password"))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenWrongDomainTokenHeaderWhenAuthenticateShouldReturnUnAuthorized() throws Exception {
        this.mockMvc.perform(
                post("/domains/{domainId}/authenticate", DOMAIN_ID)
                        .header(DOMAIN_AUTH_TOKEN_FIELD, "33333")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(
                                new AuthenticateRequest("user", "password")))).andExpect(status().isForbidden());
    }

    @Test
    public void givenCorrectDomainTokenHeaderWhenAuthenticateShouldReturnAuthorized() throws Exception {
        doReturn(true).when(domainTokenChecker).checkIfTokenMatchesDomainToken(getToken(), DOMAIN_ID);
        createAppUser();
        this.mockMvc.perform(
                post("/domains/{domainId}/authenticate", DOMAIN_ID)
                        .header(DOMAIN_AUTH_TOKEN_FIELD, getToken()).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(
                                new AuthenticateRequest("user", "password")
                                        ))).andExpect(status().isOk())
                                        .andExpect(jsonPath("$.status").value(AUTHENTICATED.name()));
    }

    private void createAppUser() {
        AppUser user = new AppUser();
        user.setExternalId("ext-01");
        user.setUsername("user");
        user.setPassword("password");
        user.setDomainId("1");
        appUserRepository.save(user);
    }

    protected String getToken(){
        return TOKEN;
    }
}
