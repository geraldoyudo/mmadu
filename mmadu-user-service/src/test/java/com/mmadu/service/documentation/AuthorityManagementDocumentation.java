package com.mmadu.service.documentation;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.service.entities.AppUser;
import com.mmadu.service.entities.Authority;
import com.mmadu.service.entities.UserAuthority;
import com.mmadu.service.repositories.AuthorityRepository;
import com.mmadu.service.repositories.UserAuthorityRepository;
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

public class AuthorityManagementDocumentation extends AbstractDocumentation {
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    private Authority authority;

    @BeforeEach
    void setUp() {
        appUserRepository.deleteAll();
        userAuthorityRepository.deleteAll();
        authorityRepository.deleteAll();
    }

    @Test
    void createAuthority() throws Exception {
        mockMvc.perform(post("/domains/{domainId}/authorities", USER_DOMAIN_ID)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, authorization("a.test-app.authority.update"))
                .content(newAuthorityRequest()))
                .andExpect(status().isCreated())
                .andDo(document(DOCUMENTATION_NAME, requestFields(
                        fieldWithPath("[].identifier").description("The authority identifier"),
                        fieldWithPath("[].name")
                                .description("The name of the authority"),
                        fieldWithPath("[].description").description("A brief description of the authority")
                ), pathParameters(
                        parameterWithName("domainId").description("The domain id of the user")
                )));
    }

    private String newAuthorityRequest() {
        ArrayNode node = objectMapper.createArrayNode();
        ObjectNode item = node.addObject()
                .put("identifier", "app.view.authority")
                .put("name", "View Entities")
                .put("description", "View all Entities");
        return node.toPrettyString();
    }

    private void createAndSaveAuthority() {
        authority = new Authority();
        authority.setDomainId(USER_DOMAIN_ID);
        authority.setIdentifier("sample");
        authority.setDescription("Sample Authority");
        authority.setName("Sample Authority");
        authority = authorityRepository.save(authority);
    }

    @Test
    void getAllAuthoritiesInADomain() throws Exception {
        createAndSaveAuthority();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/domains/{domainId}/authorities",
                USER_DOMAIN_ID)
                .param("page", "0")
                .param("size", "10")
                .header(HttpHeaders.AUTHORIZATION, authorization("a.test-app.authority.read"))
        )
                .andExpect(status().isOk())
                .andDo(document(DOCUMENTATION_NAME,
                        requestParameters(
                                parameterWithName("page").description("page number to request"),
                                parameterWithName("size").description("maximum number of items in page")
                        ),
                        pathParameters(
                                parameterWithName("domainId").description("The domain id of the authority")
                        ),
                        authorityResponseFields()));
    }

    private ResponseFieldsSnippet authorityResponseFields() {
        return relaxedResponseFields(
                fieldWithPath("content.[].name").description("The name of the authority"),
                fieldWithPath("content.[].identifier").description("The authority identifier"),
                fieldWithPath("content.[].description").description("A brief description of the authority")
        );
    }

    @Test
    void deleteAuthority() throws Exception {
        createAndSaveAuthority();
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/domains/{domainId}/authorities/{authorityIdentifier}",
                USER_DOMAIN_ID, authority.getIdentifier())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.test-app.authority.delete"))
        ).andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("domainId").description("The domain id"),
                                parameterWithName("authorityIdentifier").description("The role identifier")
                        )));
        assertTrue(
                authorityRepository.findById(authority.getId()).isEmpty()
        );
    }

    @Test
    void grantUserAuthority() throws Exception {
        createAUserAndSave();
        createAndSaveAuthority();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/domains/{domainId}/authorities/users/{userId}/addAuthorities",
                USER_DOMAIN_ID, USER_EXTERNAL_ID)
                .header(HttpHeaders.AUTHORIZATION, authorization("a.test-app.authority.grant_user"))
                .content(
                        objectMapper.createArrayNode()
                                .add(authority.getIdentifier())
                                .toPrettyString()
                )
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("domainId").description("The domain id of the authority"),
                                parameterWithName("userId").description("The id of the user")
                        )));
    }

    @Test
    void removeUserFromAuthority() throws Exception {
        createAUserAndSave();
        createAndSaveAuthority();
        setUserAuthority();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/domains/{domainId}/authorities/users/{userId}/removeAuthorities",
                USER_DOMAIN_ID, USER_EXTERNAL_ID)
                .header(HttpHeaders.AUTHORIZATION, authorization("a.test-app.authority.revoke_user"))
                .content(
                        objectMapper.createArrayNode()
                                .add(authority.getIdentifier())
                                .toPrettyString()
                )
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent())
                .andDo(document(DOCUMENTATION_NAME,
                        pathParameters(
                                parameterWithName("domainId").description("The domain id of the authority"),
                                parameterWithName("userId").description("The id of the user")
                        )));
        assertEquals(0, userAuthorityRepository.findAll().size());
    }

    private void setUserAuthority() {
        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setUser(appUserRepository.findById(TEST_USER_ID).get());
        userAuthority.setAuthority(authority);
        userAuthority.setDomainId(USER_DOMAIN_ID);
        userAuthorityRepository.save(userAuthority);
    }

    private void createAUserAndSave() {
        AppUser user = createAppUserWithConstantId();
        appUserRepository.save(user);
    }
}
