package com.mmadu.service.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.mmadu.service.utilities.DomainAuthenticationConstants.DOMAIN_AUTH_TOKEN_FIELD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "mmadu.domain.api-security-enabled=false")
public class TokenSecurityDisableTest {

    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void givenApiSecurityDisableShouldAccessApiFreely() throws Exception {
        this.mockMvc.perform(get("/appUsers")
                .header(DOMAIN_AUTH_TOKEN_FIELD, "1")
        ).andExpect(status().isOk());
    }


}
