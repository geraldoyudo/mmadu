package com.mmadu.service.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.service.entities.Group;
import com.mmadu.service.repositories.GroupRepository;
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
public class GroupResourceAccessTest {
    public static final String DOMAIN_ID = "1";
    public static final String IDENTIFIER = "test-group";
    private static TokenGeneratorUtils tokenGenerator;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GroupRepository groupRepository;

    @BeforeAll
    static void setUpClass() throws Exception {
        tokenGenerator = TokenGeneratorUtils.getInstance();
    }

    @AfterEach
    void clear() {
        groupRepository.deleteAll();
    }

    @Test
    void getGroupGloballyWithAdequateGroupShouldReturnOK() throws Exception {
        Group group = createAndSaveGroup();

        mockMvc.perform(get("/groups/" + group.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.global.group.read"))
        )
                .andExpect(status().isOk());
    }

    private Group createAndSaveGroup() {
        Group group = new Group();
        group.setDomainId(DOMAIN_ID);
        group.setName("test");
        group.setDescription("test");
        group.setIdentifier(IDENTIFIER);
        group = groupRepository.save(group);
        return group;
    }


    private String authorization(String... groups) throws Exception {
        return "Bearer " + tokenGenerator.generateTokenWithAuthorities(groups);
    }

    @Test
    void getGroupGloballyForAnotherDomainWithoutAccessShouldReturnForbidden() throws Exception {
        Group group = createAndSaveGroup();

        mockMvc.perform(get("/groups/" + group.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.group.read"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void getDomainGroupWithGroupShouldReturnBeAccessible() throws Exception {
        Group group = createAndSaveGroup();

        mockMvc.perform(get("/groups/search/findByDomainIdAndIdentifier?domainId=1&identifier=" + group.getIdentifier())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.group.read"))
        )
                .andExpect(status().isOk());
    }

    @Test
    void getDomainGroupWithDifferentGroupShouldReturnBeForbidden() throws Exception {
        Group group = createAndSaveGroup();

        mockMvc.perform(get("/groups/search/findByDomainIdAndIdentifier?domainId=1&identifier=" + group.getIdentifier())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.2.group.read"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteGroupWithGroupShouldReturnNoContent() throws Exception {
        Group group = createAndSaveGroup();
        mockMvc.perform(delete("/groups/" + group.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.group.delete"))
        )
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteGroupWithWrongGroupShouldReturnForbidden() throws Exception {
        Group group = createAndSaveGroup();
        mockMvc.perform(delete("/groups/" + group.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.group.read"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteGroupWithGroupForAnotherDomainShouldReturnForbidden() throws Exception {
        Group group = createAndSaveGroup();
        mockMvc.perform(delete("/groups/" + group.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.2.group.delete"))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void createDomainWithCorrectGroupShouldBeAccessible() throws Exception {
        mockMvc.perform(post("/groups")
                .content(newGroupRequest())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.group.create"))
        )
                .andExpect(status().isCreated());

    }

    private String newGroupRequest() {
        return objectMapper.createObjectNode()
                .put("groupname", "test")
                .put("password", "test")
                .put("domainId", DOMAIN_ID)
                .toPrettyString();
    }

    @Test
    void createDomainWithIncorrectCorrectGroupShouldBeForbidden() throws Exception {
        mockMvc.perform(post("/groups")
                .content(newGroupRequest())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.2.group.read"))
        )
                .andExpect(status().isForbidden());

    }

    private String newGroupUpdateRequest() throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of("password", "32329382"));
    }

    @Test
    void updateDomainWithCorrectGroupShouldBeAccessible() throws Exception {
        Group group = createAndSaveGroup();
        mockMvc.perform(patch("/groups/" + group.getId())
                .content(newGroupUpdateRequest())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.group.update"))
        )
                .andExpect(status().isNoContent());

    }

    @Test
    void updateDomainWithIncorrectCorrectGroupShouldBeForbidden() throws Exception {
        Group group = createAndSaveGroup();
        mockMvc.perform(patch("/groups/" + group.getId())
                .content(newGroupUpdateRequest())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.2.group.update"))
        )
                .andExpect(status().isForbidden());

    }
}
