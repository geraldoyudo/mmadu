package com.mmadu.identity.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.identity.entities.Client;
import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.repositories.ClientRepository;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
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

public class ClientDocumentation extends AbstractDocumentation {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ClientRepository clientRepository;
    @MockBean
    private DomainIdentityConfigurationService domainIdentityConfigurationService;
    @Mock
    private DomainIdentityConfiguration configuration;

    @Test
    void createNewClient() throws Exception {
        when(domainIdentityConfigurationService.findByDomainId(DOMAIN_ID)).thenReturn(Optional.of(configuration));
        mockMvc.perform(
                post("/admin/repo/clients")
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.client.create"))
                        .content(newClientRequest())
        ).andExpect(status().isCreated())
                .andDo(
                        document(DOCUMENTATION_NAME, requestFields(
                                clientFields()
                        ))
                );
    }

    private String newClientRequest() {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("name", "Email Client")
                .put("category", "third_party")
                .put("logoUrl", "http://an.email.com/favicon")
                .put("applicationUrl", "http://an.email.com")
                .put("code", "A12345")
                .put("domainId", DOMAIN_ID);
        node.putArray("tags")
                .add("email")
                .add("communications");
        return node.toPrettyString();
    }

    private static List<FieldDescriptor> clientFields() {
        return asList(
                fieldWithPath("id").type("string").optional().description("Client ID"),
                fieldWithPath("name").description("The client's name"),
                fieldWithPath("category").description("The client's category").optional(),
                fieldWithPath("tags").type(JsonFieldType.ARRAY)
                        .description("List of tags to further categorize clients"),
                fieldWithPath("logoUrl").description("The Client's logo").optional(),
                fieldWithPath("applicationUrl").description("The client's main application home page url"),
                fieldWithPath("code").description("The client code"),
                fieldWithPath("domainId").description("the ID of the domain")
        );
    }

    @Test
    void getClientById() throws Exception {
        Client client = newClient();
        client = clientRepository.save(client);
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/admin/repo/clients/{clientId}", client.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.global.client.read"))
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                clientIdParameter()
                        ), relaxedResponseFields(
                                clientFields()
                        ))
                );
    }

    private Client newClient() throws com.fasterxml.jackson.core.JsonProcessingException {
        return objectMapper.readValue(newClientRequest(), Client.class);
    }

    private ParameterDescriptor clientIdParameter() {
        return parameterWithName("clientId").description("The client ID");
    }

    @Test
    public void getClientsByDomain() throws Exception {
        Client client = clientRepository.save(newClient());
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/admin/repo/clients/search/findByDomainId")
                        .param("domainId", client.getDomainId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.client.read"))
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, requestParameters(
                                parameterWithName("domainId").description("Domain ID of the client")
                        ), relaxedResponseFields(
                                clientListFields()
                        ))
                );
    }

    private static List<FieldDescriptor> clientListFields() {
        return asList(
                fieldWithPath("_embedded.clients.[].id").description("The client id"),
                fieldWithPath("_embedded.clients.[].domainId").description("the client domain id"),
                fieldWithPath("_embedded.clients.[].name").description("The client name"),
                fieldWithPath("_embedded.clients.[].code").description("The client code"),
                fieldWithPath("_embedded.clients.[].applicationUrl").description("The client's application url"),
                fieldWithPath("_embedded.clients.[].logoUrl").description("The client's logo url"),
                fieldWithPath("_embedded.clients.[].category").description("The client's category"),
                fieldWithPath("_embedded.clients.[].tags").description("The client's tags")
        );
    }

    @Test
    public void updateClientById() throws Exception {
        when(domainIdentityConfigurationService.findByDomainId(DOMAIN_ID)).thenReturn(Optional.of(configuration));
        final String newClientName = "New Email Client";
        Client client = clientRepository.save(newClient());
        mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/admin/repo/clients/{clientId}", client.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.client.update"))
                        .content(
                                objectMapper.createObjectNode()
                                        .put("name", newClientName)
                                        .toString()
                        )
        ).andExpect(status().isNoContent())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                clientIdParameter()
                        ))
                );
        assertThat(clientRepository.findById(client.getId()).get().getName(), equalTo(newClientName));
    }

    @Test
    public void deleteClientById() throws Exception {
        Client client = clientRepository.save(newClient());
        mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/admin/repo/clients/{clientId}", client.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.client.delete"))
        ).andExpect(status().isNoContent())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                clientIdParameter()
                        ))
                );
        assertThat(clientRepository.existsById(client.getId()), equalTo(false));
    }
}
