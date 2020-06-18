package com.mmadu.service.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.service.entities.AppUser;
import com.mmadu.service.repositories.AppUserRepository;
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
public class UserResourceAccessTest {
    public static final String DOMAIN_ID = "1";
    public static final String EXTERNAL_ID = "2323";
    private static TokenGeneratorUtils tokenGenerator;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeAll
    static void setUpClass() throws Exception {
        tokenGenerator = TokenGeneratorUtils.getInstance();
    }

    @AfterEach
    void clear() {
        appUserRepository.deleteAll();
    }

    @Test
    void getUserGloballyWithAdequateAuthorityShouldReturnOK() throws Exception {
        AppUser user = createAndSaveAppUser();

        mockMvc.perform(get("/appUsers/" + user.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.global.user.read"))
        )
                .andExpect(status().isOk());
    }

    private AppUser createAndSaveAppUser() {
        AppUser user = new AppUser();
        user.setPassword("password");
        user.setDomainId(DOMAIN_ID);
        user.setUsername("test");
        user.setExternalId(EXTERNAL_ID);
        user = appUserRepository.save(user);
        return user;
    }


    private String authorization(String... authorities) throws Exception {
        return "Bearer " + tokenGenerator.generateTokenWithAuthorities(authorities);
    }

    @Test
    void getUserGloballyForAnotherDomainWithoutAccessShouldReturnForbidden() throws Exception {
        AppUser user = createAndSaveAppUser();

        mockMvc.perform(get("/appUsers/" + user.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.user.read"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void getDomainUserWithAuthorityShouldReturnBeAccessible() throws Exception {
        AppUser user = createAndSaveAppUser();

        mockMvc.perform(get("/appUsers/search/findByDomainIdAndExternalId?domainId=1&externalId=" + user.getExternalId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.user.read"))
        )
                .andExpect(status().isOk());
    }

    @Test
    void getDomainUserWithDifferentAuthorityShouldReturnBeForbidden() throws Exception {
        AppUser user = createAndSaveAppUser();

        mockMvc.perform(get("/appUsers/search/findByDomainIdAndExternalId?domainId=1&externalId=" + user.getExternalId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.2.user.read"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUserWithAuthorityShouldReturnNoContent() throws Exception {
        AppUser user = createAndSaveAppUser();
        mockMvc.perform(delete("/appUsers/" + user.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.user.delete"))
        )
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUserWithWrongAuthorityShouldReturnForbidden() throws Exception {
        AppUser user = createAndSaveAppUser();
        mockMvc.perform(delete("/appUsers/" + user.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.user.read"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUserWithAuthorityForAnotherDomainShouldReturnForbidden() throws Exception {
        AppUser user = createAndSaveAppUser();
        mockMvc.perform(delete("/appUsers/" + user.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.2.user.delete"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void createDomainWithCorrectAuthorityShouldBeAccessible() throws Exception {
        mockMvc.perform(post("/appUsers")
                .content(newUserRequest())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.user.create"))
        )
                .andExpect(status().isCreated());

    }

    private String newUserRequest() {
        return objectMapper.createObjectNode()
                .put("username", "test")
                .put("password", "test")
                .put("domainId", DOMAIN_ID)
                .toPrettyString();
    }

    @Test
    void createDomainWithIncorrectCorrectAuthorityShouldBeForbidden() throws Exception {
        mockMvc.perform(post("/appUsers")
                .content(newUserRequest())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.2.user.read"))
        )
                .andExpect(status().isForbidden());

    }

    private String newUserUpdateRequest() throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of("password", "32329382"));
    }

    @Test
    void updateDomainWithCorrectAuthorityShouldBeAccessible() throws Exception {
        AppUser user = createAndSaveAppUser();
        mockMvc.perform(patch("/appUsers/" + user.getId())
                .content(newUserUpdateRequest())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.user.update"))
        )
                .andExpect(status().isNoContent());

    }

    @Test
    void updateDomainWithIncorrectCorrectAuthorityShouldBeForbidden() throws Exception {
        AppUser user = createAndSaveAppUser();
        mockMvc.perform(patch("/appUsers/" + user.getId())
                .content(newUserUpdateRequest())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.2.user.update"))
        )
                .andExpect(status().isForbidden());

    }
}
