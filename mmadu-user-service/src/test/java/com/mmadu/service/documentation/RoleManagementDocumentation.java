package com.mmadu.service.documentation;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.service.entities.*;
import com.mmadu.service.repositories.AuthorityRepository;
import com.mmadu.service.repositories.RoleAuthorityRepository;
import com.mmadu.service.repositories.RoleRepository;
import com.mmadu.service.repositories.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoleManagementDocumentation extends AbstractDocumentation {
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private RoleAuthorityRepository roleAuthorityRepository;

    private Role role;

    @BeforeEach
    void setUp() {
        appUserRepository.deleteAll();
        userRoleRepository.deleteAll();
        roleRepository.deleteAll();
        authorityRepository.deleteAll();
        roleAuthorityRepository.deleteAll();
        ;
    }

    @Test
    void createRole() throws Exception {
        createAndSaveAuthority();
        mockMvc.perform(post("/domains/{domainId}/roles", USER_DOMAIN_ID)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ADMIN_TOKEN)
                .content(newRoleRequest()))
                .andExpect(status().isCreated())
                .andDo(document(DOCUMENTATION_NAME, requestFields(
                        fieldWithPath("[].identifier").description("The role identifier"),
                        fieldWithPath("[].name")
                                .description("The name of the role"),
                        fieldWithPath("[].description").description("A brief description of the role"),
                        fieldWithPath("[].authorities").description("A list of authority identifiers to be granted to this role")
                ), pathParameters(
                        parameterWithName("domainId").description("The domain id of the user")
                )));
    }

    private Authority createAndSaveAuthority() {
        Authority authority = new Authority();
        authority.setDomainId(USER_DOMAIN_ID);
        authority.setDescription("test-auth-description");
        authority.setIdentifier("test-auth");
        authority.setName("test-auth-name");
        return authorityRepository.save(authority);
    }

    private String newRoleRequest() {
        ArrayNode node = objectMapper.createArrayNode();
        ObjectNode item = node.addObject()
                .put("identifier", "app.view")
                .put("name", "View Entities")
                .put("description", "View all Entities");
        item.putArray("authorities")
                .add("test-auth");
        return node.toPrettyString();
    }

    private void createAndSaveRole() {
        role = new Role();
        role.setDomainId(USER_DOMAIN_ID);
        role.setIdentifier("sample");
        role.setDescription("Sample Role");
        role.setName("Sample Role");
        role = roleRepository.save(role);
    }

    @Test
    void getAllRolesInADomain() throws Exception {
        createAndSaveRole();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/domains/{domainId}/roles",
                USER_DOMAIN_ID)
                .param("page", "0")
                .param("size", "10")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ADMIN_TOKEN)
        )
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME,
                        requestParameters(
                                parameterWithName("page").description("page number to request"),
                                parameterWithName("size").description("maximum number of items in page")
                        ),
                        pathParameters(
                                parameterWithName("domainId").description("The domain id of the role")
                        ),
                        roleResponseFields()));
    }

    private ResponseFieldsSnippet roleResponseFields() {
        return relaxedResponseFields(
                fieldWithPath("content.[].name").description("The name of the role"),
                fieldWithPath("content.[].identifier").description("The role identifier"),
                fieldWithPath("content.[].description").description("A brief description of the role")
        );
    }

    @Test
    void deleteRole() throws Exception {
        createAndSaveRole();
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/domains/{domainId}/roles/{roleIdentifier}",
                USER_DOMAIN_ID, role.getIdentifier())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ADMIN_TOKEN)
        ).andExpect(status().isNoContent());
        assertTrue(
                roleRepository.findById(role.getId()).isEmpty()
        );
    }

    @Test
    void grantUserRole() throws Exception {
        createAUserAndSave();
        createAndSaveRole();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/domains/{domainId}/roles/users/{userId}/addRoles",
                USER_DOMAIN_ID, USER_EXTERNAL_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ADMIN_TOKEN)
                .content(
                        objectMapper.createArrayNode()
                                .add(role.getIdentifier())
                                .toPrettyString()
                )
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("domainId").description("The domain id of the role"),
                                parameterWithName("userId").description("The id of the user")
                        )));
    }

    @Test
    void removeUserFromRole() throws Exception {
        createAUserAndSave();
        createAndSaveRole();
        setUserRole();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/domains/{domainId}/roles/users/{userId}/removeRoles",
                USER_DOMAIN_ID, USER_EXTERNAL_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ADMIN_TOKEN)
                .content(
                        objectMapper.createArrayNode()
                                .add(role.getIdentifier())
                                .toPrettyString()
                )
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("domainId").description("The domain id of the role"),
                                parameterWithName("userId").description("The id of the user")
                        )));
        assertEquals(0, userRoleRepository.findAll().size());
    }

    private void setUserRole() {
        UserRole userRole = new UserRole();
        userRole.setUser(appUserRepository.findById(TEST_USER_ID).get());
        userRole.setRole(role);
        userRole.setDomainId(USER_DOMAIN_ID);
        userRoleRepository.save(userRole);
    }

    private void createAUserAndSave() {
        AppUser user = createAppUserWithConstantId();
        appUserRepository.save(user);
    }

    @Test
    void addAuthoritiesToRole() throws Exception {
        createAndSaveRole();
        Authority authority = createAndSaveAuthority();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/domains/{domainId}/roles/authorities/add",
                USER_DOMAIN_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ADMIN_TOKEN)
                .content(
                        roleAuthorityUpdateRequest(authority)
                )
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("domainId").description("The domain id of the role")
                        )));
    }

    private String roleAuthorityUpdateRequest(Authority authority) {
        ArrayNode array = objectMapper.createArrayNode();
        array.addObject()
                .put("role", role.getIdentifier())
                .putArray("authorities")
                .add(authority.getIdentifier());
        return array.toPrettyString();
    }

    @Test
    void removeAuthoritiesFromRole() throws Exception {
        createAndSaveRole();
        Authority authority = createAndSaveAuthority();
        linkAuthorityToRole(authority);
        mockMvc.perform(RestDocumentationRequestBuilders.post("/domains/{domainId}/roles/authorities/remove",
                USER_DOMAIN_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ADMIN_TOKEN)
                .content(
                        roleAuthorityUpdateRequest(authority)
                )
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("domainId").description("The domain id of the role")
                        )));
        assertEquals(0, roleAuthorityRepository.findAll().size());
    }

    private void linkAuthorityToRole(Authority authority) {
        RoleAuthority ra = new RoleAuthority();
        ra.setDomainId(USER_DOMAIN_ID);
        ra.setRole(role);
        ra.setAuthority(authority);
        roleAuthorityRepository.save(ra);
    }
}
