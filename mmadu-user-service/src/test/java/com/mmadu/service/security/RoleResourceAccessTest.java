package com.mmadu.service.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.service.entities.Role;
import com.mmadu.service.repositories.RoleRepository;
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
public class RoleResourceAccessTest {
    public static final String DOMAIN_ID = "1";
    public static final String IDENTIFIER = "test-role";
    private static TokenGeneratorUtils tokenGenerator;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeAll
    static void setUpClass() throws Exception {
        tokenGenerator = TokenGeneratorUtils.getInstance();
    }

    @AfterEach
    void clear() {
        roleRepository.deleteAll();
    }

    @Test
    void getRoleGloballyWithAdequateRoleShouldReturnOK() throws Exception {
        Role role = createAndSaveRole();

        mockMvc.perform(get("/roles/" + role.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.global.role.read"))
        )
                .andExpect(status().isOk());
    }

    private Role createAndSaveRole() {
        Role role = new Role();
        role.setDomainId(DOMAIN_ID);
        role.setName("test");
        role.setDescription("test");
        role.setIdentifier(IDENTIFIER);
        role = roleRepository.save(role);
        return role;
    }


    private String authorization(String... roles) throws Exception {
        return "Bearer " + tokenGenerator.generateTokenWithAuthorities(roles);
    }

    @Test
    void getRoleGloballyForAnotherDomainWithoutAccessShouldReturnForbidden() throws Exception {
        Role role = createAndSaveRole();

        mockMvc.perform(get("/roles/" + role.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.role.read"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void getDomainRoleWithRoleShouldReturnBeAccessible() throws Exception {
        Role role = createAndSaveRole();

        mockMvc.perform(get("/roles/search/findByDomainIdAndIdentifier?domainId=1&identifier=" + role.getIdentifier())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.role.read"))
        )
                .andExpect(status().isOk());
    }

    @Test
    void getDomainRoleWithDifferentRoleShouldReturnBeForbidden() throws Exception {
        Role role = createAndSaveRole();

        mockMvc.perform(get("/roles/search/findByDomainIdAndIdentifier?domainId=1&identifier=" + role.getIdentifier())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.2.role.read"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteRoleWithRoleShouldReturnNoContent() throws Exception {
        Role role = createAndSaveRole();
        mockMvc.perform(delete("/roles/" + role.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.role.delete"))
        )
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteRoleWithWrongRoleShouldReturnForbidden() throws Exception {
        Role role = createAndSaveRole();
        mockMvc.perform(delete("/roles/" + role.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.role.read"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteRoleWithRoleForAnotherDomainShouldReturnForbidden() throws Exception {
        Role role = createAndSaveRole();
        mockMvc.perform(delete("/roles/" + role.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.2.role.delete"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void createDomainWithCorrectRoleShouldBeAccessible() throws Exception {
        mockMvc.perform(post("/roles")
                .content(newRoleRequest())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.role.create"))
        )
                .andExpect(status().isCreated());

    }

    private String newRoleRequest() {
        return objectMapper.createObjectNode()
                .put("rolename", "test")
                .put("password", "test")
                .put("domainId", DOMAIN_ID)
                .toPrettyString();
    }

    @Test
    void createDomainWithIncorrectCorrectRoleShouldBeForbidden() throws Exception {
        mockMvc.perform(post("/roles")
                .content(newRoleRequest())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.2.role.read"))
        )
                .andExpect(status().isForbidden());

    }

    private String newRoleUpdateRequest() throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of("password", "32329382"));
    }

    @Test
    void updateDomainWithCorrectRoleShouldBeAccessible() throws Exception {
        Role role = createAndSaveRole();
        mockMvc.perform(patch("/roles/" + role.getId())
                .content(newRoleUpdateRequest())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.role.update"))
        )
                .andExpect(status().isNoContent());

    }

    @Test
    void updateDomainWithIncorrectCorrectRoleShouldBeForbidden() throws Exception {
        Role role = createAndSaveRole();
        mockMvc.perform(patch("/roles/" + role.getId())
                .content(newRoleUpdateRequest())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.2.role.update"))
        )
                .andExpect(status().isForbidden());

    }
}
