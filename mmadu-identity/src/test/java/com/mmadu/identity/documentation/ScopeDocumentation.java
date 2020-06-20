package com.mmadu.identity.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.entities.Scope;
import com.mmadu.identity.repositories.ScopeRepository;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ScopeDocumentation extends AbstractDocumentation {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ScopeRepository scopeRepository;
    @MockBean
    private DomainIdentityConfigurationService domainIdentityConfigurationService;
    @Mock
    private DomainIdentityConfiguration configuration;

    @Test
    void createNewScope() throws Exception {
        when(domainIdentityConfigurationService.findByDomainId(DOMAIN_ID)).thenReturn(Optional.of(configuration));
        mockMvc.perform(
                post("/admin/repo/scopes")
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.scope.create"))
                        .content(newScopeRequest())
        ).andExpect(status().isCreated())
                .andDo(
                        document(DOCUMENTATION_NAME, requestFields(
                                scopeFields()
                        ))
                );
    }

    private String newScopeRequest() {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("name", "Email Scope")
                .put("code", "test.mail")
                .put("description", "Email Contacts")
                .put("domainId", DOMAIN_ID)
                .putArray("authorities").add("view_users");
        return node.toPrettyString();
    }

    private static List<FieldDescriptor> scopeFields() {
        return asList(
                fieldWithPath("id").type("string").optional().description("Scope ID"),
                fieldWithPath("name").description("The scope's name"),
                fieldWithPath("code").description("The scopes identification code"),
                fieldWithPath("authorities").description("List of authorities and roles associated with this scope"),
                fieldWithPath("description").description("The scopes description"),
                fieldWithPath("domainId").description("The domain id of the scope")
        );
    }

    @Test
    void getScopeById() throws Exception {
        Scope scope = newScope();
        scope = scopeRepository.save(scope);
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/admin/repo/scopes/{scopeId}", scope.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.global.scope.read"))
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                scopeIdParameter()
                        ), relaxedResponseFields(
                                scopeFields()
                        ))
                );
    }

    private Scope newScope() throws com.fasterxml.jackson.core.JsonProcessingException {
        return objectMapper.readValue(newScopeRequest(), Scope.class);
    }

    private ParameterDescriptor scopeIdParameter() {
        return parameterWithName("scopeId").description("The scope ID");
    }

    @Test
    public void getScopesByDomain() throws Exception {
        Scope scope = scopeRepository.save(newScope());
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/admin/repo/scopes/search/findByDomainId")
                        .param("domainId", scope.getDomainId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.scope.read"))
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, requestParameters(
                                parameterWithName("domainId").description("Domain ID of the scope")
                        ), relaxedResponseFields(
                                scopeListFields()
                        ))
                );
    }

    private static List<FieldDescriptor> scopeListFields() {
        return asList(
                fieldWithPath("_embedded.scopes.[].id").description("The scope id"),
                fieldWithPath("_embedded.scopes.[].domainId").description("the scope domain id"),
                fieldWithPath("_embedded.scopes.[].name").description("The scope name"),
                fieldWithPath("_embedded.scopes.[].code").description("The scope code"),
                fieldWithPath("_embedded.scopes.[].authorities").description("List of authorities and roles associated with this scope"),
                fieldWithPath("_embedded.scopes.[].description").description("The scope's description")
        );
    }

    @Test
    public void updateScopeById() throws Exception {
        when(domainIdentityConfigurationService.findByDomainId(DOMAIN_ID)).thenReturn(Optional.of(configuration));
        final String newScopeName = "New Email";
        Scope scope = scopeRepository.save(newScope());
        mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/admin/repo/scopes/{scopeId}", scope.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.scope.update"))
                        .content(
                                objectMapper.createObjectNode()
                                        .put("name", newScopeName)
                                        .toString()
                        )
        ).andExpect(status().isNoContent())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                scopeIdParameter()
                        ))
                );
        assertThat(scopeRepository.findById(scope.getId()).get().getName(), equalTo(newScopeName));
    }

    @Test
    public void deleteScopeById() throws Exception {
        Scope scope = scopeRepository.save(newScope());
        mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/admin/repo/scopes/{scopeId}", scope.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.scope.delete"))
        ).andExpect(status().isNoContent())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                scopeIdParameter()
                        ))
                );
        assertThat(scopeRepository.existsById(scope.getId()), equalTo(false));
    }
}
