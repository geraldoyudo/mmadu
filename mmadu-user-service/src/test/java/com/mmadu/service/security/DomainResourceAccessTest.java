package com.mmadu.service.security;

import com.mmadu.service.utilities.TokenGeneratorUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DomainResourceAccessTest {
    private static TokenGeneratorUtils tokenGenerator;


    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setUpClass() throws Exception {
        tokenGenerator = TokenGeneratorUtils.getInstance();
    }

    @Test
    void getDomainWithAdequateAuthorityShouldReturnOK() throws Exception {
        mockMvc.perform(get("/appDomains/1")
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.domain.read"))
        )
                .andExpect(status().isOk());
    }


    private String authorization(String... authorities) throws Exception {
        return "Bearer " + tokenGenerator.generateTokenWithAuthorities(authorities);
    }

    @Test
    void getDomainForAnotherDomainWithoutAccessShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/appDomains/2")
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.domain.read"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void getDomainWithDifferentAuthorityShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/appDomains/1")
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.domain.create"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteDomainWithAuthorityShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/appDomains/1")
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.domain.delete"))
        )
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteDomainWithWrongAuthorityShouldReturnForbidden() throws Exception {
        mockMvc.perform(delete("/appDomains/1")
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.domain.read"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteDomainWithAuthorityForAnotherDomainShouldReturnForbidden() throws Exception {
        mockMvc.perform(delete("/appDomains/1")
                .header(HttpHeaders.AUTHORIZATION, authorization("a.2.domain.delete"))
        )
                .andExpect(status().isForbidden());
    }
}
