package com.mmadu.service.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.service.entities.Authority;
import com.mmadu.service.repositories.AuthorityRepository;
import com.mmadu.service.utilities.TokenGeneratorUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorityResourceAccessTest {
    public static final String DOMAIN_ID = "1";
    public static final String IDENTIFIER = "test-authority";
    private static TokenGeneratorUtils tokenGenerator;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorityRepository authorityRepository;

    @BeforeAll
    static void setUpClass() throws Exception {
        tokenGenerator = TokenGeneratorUtils.getInstance();
    }

    @AfterEach
    void clear() {
        authorityRepository.deleteAll();
    }

    @Test
    void getAuthorityGloballyWithAdequateAuthorityShouldReturnOK() throws Exception {
        Authority authority = createAndSaveAuthority();

        mockMvc.perform(get("/authorities/" + authority.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.global.authority.read"))
        )
                .andExpect(status().isOk());
    }

    private Authority createAndSaveAuthority() {
        Authority authority = new Authority();
        authority.setDomainId(DOMAIN_ID);
        authority.setName("test");
        authority.setDescription("test");
        authority.setIdentifier(IDENTIFIER);
        authority = authorityRepository.save(authority);
        return authority;
    }


    private String authorization(String... authorities) throws Exception {
        return "Bearer " + tokenGenerator.generateTokenWithAuthorities(authorities);
    }

    @Test
    void getAuthorityGloballyForAnotherDomainWithoutAccessShouldReturnForbidden() throws Exception {
        Authority authority = createAndSaveAuthority();

        mockMvc.perform(get("/authorities/" + authority.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.authority.read"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void getDomainAuthorityWithAuthorityShouldReturnBeAccessible() throws Exception {
        Authority authority = createAndSaveAuthority();

        mockMvc.perform(get("/authorities/search/findByDomainIdAndIdentifier?domainId=1&identifier=" + authority.getIdentifier())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.authority.read"))
        )
                .andExpect(status().isOk());
    }

    @Test
    void getDomainAuthorityWithDifferentAuthorityShouldReturnBeForbidden() throws Exception {
        Authority authority = createAndSaveAuthority();

        mockMvc.perform(get("/authorities/search/findByDomainIdAndIdentifier?domainId=1&identifier=" + authority.getIdentifier())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.2.authority.read"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteAuthorityWithAuthorityShouldReturnNoContent() throws Exception {
        Authority authority = createAndSaveAuthority();
        mockMvc.perform(delete("/authorities/" + authority.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.authority.delete"))
        )
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAuthorityWithWrongAuthorityShouldReturnForbidden() throws Exception {
        Authority authority = createAndSaveAuthority();
        mockMvc.perform(delete("/authorities/" + authority.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.authority.read"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteAuthorityWithAuthorityForAnotherDomainShouldReturnForbidden() throws Exception {
        Authority authority = createAndSaveAuthority();
        mockMvc.perform(delete("/authorities/" + authority.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.2.authority.delete"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void createDomainWithCorrectAuthorityShouldBeAccessible() throws Exception {
        mockMvc.perform(post("/authorities")
                .content(newAuthorityRequest())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.authority.create"))
        )
                .andExpect(status().isCreated());

    }

    private String newAuthorityRequest() {
        return objectMapper.createObjectNode()
                .put("authorityname", "test")
                .put("password", "test")
                .put("domainId", DOMAIN_ID)
                .toPrettyString();
    }

    @Test
    void createDomainWithIncorrectCorrectAuthorityShouldBeForbidden() throws Exception {
        mockMvc.perform(post("/authorities")
                .content(newAuthorityRequest())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.2.authority.read"))
        )
                .andExpect(status().isForbidden());

    }

    private String newAuthorityUpdateRequest() throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of("password", "32329382"));
    }

    @Test
    void updateDomainWithCorrectAuthorityShouldBeAccessible() throws Exception {
        Authority authority = createAndSaveAuthority();
        mockMvc.perform(patch("/authorities/" + authority.getId())
                .content(newAuthorityUpdateRequest())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.authority.update"))
        )
                .andExpect(status().isNoContent());

    }

    @Test
    void updateDomainWithIncorrectCorrectAuthorityShouldBeForbidden() throws Exception {
        Authority authority = createAndSaveAuthority();
        mockMvc.perform(patch("/authorities/" + authority.getId())
                .content(newAuthorityUpdateRequest())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.2.authority.update"))
        )
                .andExpect(status().isForbidden());

    }
}
