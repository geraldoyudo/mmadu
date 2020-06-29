package com.mmadu.identity.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.entities.Resource;
import com.mmadu.identity.repositories.ResourceRepository;
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

public class ResourceDocumentation extends AbstractDocumentation {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ResourceRepository resourceRepository;
    @MockBean
    private DomainIdentityConfigurationService domainIdentityConfigurationService;
    @Mock
    private DomainIdentityConfiguration configuration;

    @Test
    void createNewResource() throws Exception {
        when(domainIdentityConfigurationService.findByDomainId(DOMAIN_ID)).thenReturn(Optional.of(configuration));
        mockMvc.perform(
                post("/admin/repo/resources")
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.resource.create"))
                        .content(newResourceRequest())
        ).andExpect(status().isCreated())
                .andDo(
                        document(DOCUMENTATION_NAME, requestFields(
                                resourceFields()
                        ))
                );
    }

    private String newResourceRequest() {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("name", "email")
                .put("identifier", "email-service")
                .put("description", "Email Microservice")
                .put("domainId", DOMAIN_ID);
        return node.toPrettyString();
    }

    private static List<FieldDescriptor> resourceFields() {
        return asList(
                fieldWithPath("id").type("string").optional().description("Resource ID"),
                fieldWithPath("name").description("The resource's name"),
                fieldWithPath("identifier").description("The resource's identifier"),
                fieldWithPath("description").description("A description of the resource"),
                fieldWithPath("domainId").description("The domain ID")
        );
    }

    @Test
    void getResourceById() throws Exception {
        Resource resource = newResource();
        resource = resourceRepository.save(resource);
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/admin/repo/resources/{resourceId}", resource.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.global.resource.read"))
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                resourceIdParameter()
                        ), relaxedResponseFields(
                                resourceFields()
                        ))
                );
    }

    private Resource newResource() throws com.fasterxml.jackson.core.JsonProcessingException {
        return objectMapper.readValue(newResourceRequest(), Resource.class);
    }

    private ParameterDescriptor resourceIdParameter() {
        return parameterWithName("resourceId").description("The resource ID");
    }

    @Test
    public void getResourcesByDomain() throws Exception {
        Resource resource = resourceRepository.save(newResource());
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/admin/repo/resources/search/findByDomainId")
                        .param("domainId", resource.getDomainId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.resource.read"))
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, requestParameters(
                                parameterWithName("domainId").description("Domain ID of the resource")
                        ), relaxedResponseFields(
                                resourceListFields()
                        ))
                );
    }

    private static List<FieldDescriptor> resourceListFields() {
        return asList(
                fieldWithPath("_embedded.resources.[].id").description("The resource id"),
                fieldWithPath("_embedded.resources.[].domainId").description("the resource domain id"),
                fieldWithPath("_embedded.resources.[].name").description("The resource name"),
                fieldWithPath("_embedded.resources.[].identifier").description("The resource identifier"),
                fieldWithPath("_embedded.resources.[].description").description("A description of the resource")
        );
    }

    @Test
    public void updateResourceById() throws Exception {
        when(domainIdentityConfigurationService.findByDomainId(DOMAIN_ID)).thenReturn(Optional.of(configuration));
        final String newResourceName = "New Email Resource";
        Resource resource = resourceRepository.save(newResource());
        mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/admin/repo/resources/{resourceId}", resource.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.resource.update"))
                        .content(
                                objectMapper.createObjectNode()
                                        .put("name", newResourceName)
                                        .toString()
                        )
        ).andExpect(status().isNoContent())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                resourceIdParameter()
                        ))
                );
        assertThat(resourceRepository.findById(resource.getId()).get().getName(), equalTo(newResourceName));
    }

    @Test
    public void deleteResourceById() throws Exception {
        Resource resource = resourceRepository.save(newResource());
        mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/admin/repo/resources/{resourceId}", resource.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.resource.delete"))
        ).andExpect(status().isNoContent())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                resourceIdParameter()
                        ))
                );
        assertThat(resourceRepository.existsById(resource.getId()), equalTo(false));
    }
}
