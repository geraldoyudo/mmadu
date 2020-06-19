package com.mmadu.identity.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.identity.entities.*;
import com.mmadu.identity.repositories.ClientInstanceRepository;
import com.mmadu.identity.repositories.ClientRepository;
import com.mmadu.identity.repositories.ResourceRepository;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import com.mmadu.identity.utils.ClientProfileUtils;
import com.mmadu.identity.utils.GrantTypeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;

import java.util.Collections;
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

public class ClientInstanceDocumentation extends AbstractDocumentation {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientInstanceRepository clientInstanceRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    private Client client;
    private Resource resource;
    @MockBean
    private DomainIdentityConfigurationService domainIdentityConfigurationService;
    @Mock
    private DomainIdentityConfiguration configuration;

    @BeforeEach
    void setUp() {
        createClient();
        createResource();
    }

    private void createClient() {
        client = new Client();
        client.setApplicationUrl("https://myapp.com");
        client.setCategory("test");
        client.setCode("1234");
        client.setDomainId("1");
        client.setName("test");
        client.setTags(Collections.singletonList("tags"));
        client.setLogoUrl("https://logo.url/favicon");
        client = clientRepository.save(client);
    }

    private void createResource() {
        resource = new Resource();
        resource.setDescription("test resource");
        resource.setDomainId("1");
        resource.setIdentifier("test");
        resource.setName("test");
        resource = resourceRepository.save(resource);
    }

    @Test
    void createNewClientInstance() throws Exception {
        when(domainIdentityConfigurationService.findByDomainId(DOMAIN_ID)).thenReturn(Optional.of(configuration));
        mockMvc.perform(
                post("/admin/repo/clientInstances")
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.client_instance.create"))
                        .content(newClientInstanceRequest())
        ).andExpect(status().isCreated())
                .andDo(
                        document(DOCUMENTATION_NAME, requestFields(
                                clientInstanceFields()
                        ))
                );
    }

    private String newClientInstanceRequest() {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("clientId", client.getId())
                .put("clientType", "CONFIDENTIAL")
                .put("clientProfile", "web_app")
                .put("tlsEnabled", true)
                .put("includeUserRoles", true)
                .put("includeUserAuthorities", true)
                .put("domainId", DOMAIN_ID);
        node.putArray("resources")
                .add(resource.getIdentifier());
        node.putArray("authorities")
                .add("admin");
        node.putArray("redirectionUris")
                .add("https://myapp.com/callback")
                .add("https://localhost:8080/callback");
        node.putArray("allowedHosts")
                .add("localhost")
                .add("teamapt.com")
                .add("32.32.182.34");
        node.putArray("supportedGrantTypes")
                .add("authentication_code")
                .add("client_credentials");
        ObjectNode credentialsNode = node.putObject("credentials")
                .put("type", "secret")
                .put("secret", "1234");
        return node.toPrettyString();
    }

    private static List<FieldDescriptor> clientInstanceFields() {
        return asList(
                fieldWithPath("id").type("string").optional().description("Client ID"),
                fieldWithPath("clientId").description("The client ID"),
                fieldWithPath("clientType").description("Either CONFIDENTIAL or PUBLIC").optional(),
                fieldWithPath("clientProfile").description("Client profile category: either web_app, user_agent_app, or native_app, custom profiles may be used."),
                fieldWithPath("credentials").type(JsonFieldType.VARIES).description("Client credentials").optional(),
                fieldWithPath("identifier").type(JsonFieldType.STRING).description("The client's generated identifier used for authorization and authentication"),
                fieldWithPath("allowedHosts").type(JsonFieldType.ARRAY).description("The host names to be used with this client"),
                fieldWithPath("redirectionUris").type(JsonFieldType.ARRAY).description("The redirection urls permitted to be used with this client"),
                fieldWithPath("supportedGrantTypes").type(JsonFieldType.ARRAY).description("The grant types these clients are permitted to use"),
                fieldWithPath("tlsEnabled").type(JsonFieldType.BOOLEAN).description("Whether TLS should be made compulsory"),
                fieldWithPath("domainId").description("the ID of the domain"),
                fieldWithPath("resources").description("The resource ids of the resources the client should have access to"),
                fieldWithPath("authorities").description("The list of authorities granted to the client"),
                fieldWithPath("includeUserRoles").description("Include user's roles in the token info"),
                fieldWithPath("includeUserAuthorities").description("Include user's authorities in the token info"),
                fieldWithPath("credentials.type").description("The client's credential type (for now, `secret`)"),
                fieldWithPath("credentials.secret").description("The client secret (if credential type is `secret`)").optional()
        );
    }

    @Test
    void getClientInstanceById() throws Exception {
        ClientInstance instance = newClientInstance();
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/admin/repo/clientInstances/{clientInstanceId}", instance.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.global.client_instance.read"))
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                clientInstanceIdParameter()
                        ), relaxedResponseFields(
                                clientInstanceFields()
                        ))
                );
    }

    private ClientInstance newClientInstance() {
        ClientInstance instance = new ClientInstance();
        instance.setClientId(client.getId());
        instance.setClientType(ClientType.CONFIDENTIAL);
        instance.setClientProfile(ClientProfileUtils.WEB_APP);
        instance.setCredentials(new ClientSecretCredentials("1234"));
        instance.setIdentifier("1111");
        instance.setRedirectionUris(List.of("https://redirect.com/callback", "https://localhost:832/callback"));
        instance.setAllowedHosts(List.of("192.168.99.100"));
        instance.setTlsEnabled(true);
        instance.setSupportedGrantTypes(List.of(GrantTypeUtils.AUTHORIZATION_CODE, GrantTypeUtils.CLIENT_CREDENTIALS));
        instance.setDomainId("1");
        instance.setAuthorities(List.of("admin"));
        instance.setResources(List.of(resource.getIdentifier()));
        return clientInstanceRepository.save(instance);
    }

    private ParameterDescriptor clientInstanceIdParameter() {
        return parameterWithName("clientInstanceId").description("The client instance ID");
    }

    @Test
    public void getClientInstancesByDomain() throws Exception {
        ClientInstance instance = newClientInstance();
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/admin/repo/clientInstances/search/findByDomainId")
                        .param("domainId", instance.getDomainId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.client_instance.read"))
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, requestParameters(
                                parameterWithName("domainId").description("Domain ID of the client instance")
                        ), relaxedResponseFields(
                                clientInstanceListFields()
                        ))
                );
    }

    private static List<FieldDescriptor> clientInstanceListFields() {
        return asList(
                fieldWithPath("_embedded.clientInstances.[].id").description("The client instance id"),
                fieldWithPath("_embedded.clientInstances.[].domainId").description("the client instance domain id"),
                fieldWithPath("_embedded.clientInstances.[].clientType").description("The client instance type"),
                fieldWithPath("_embedded.clientInstances.[].clientProfile").description("The client instance profile"),
                fieldWithPath("_embedded.clientInstances.[].identifier").description("The client's identifier (used for authentication and authorization)"),
                fieldWithPath("_embedded.clientInstances.[].redirectionUris").description("The client's allowed redirection uris"),
                fieldWithPath("_embedded.clientInstances.[].allowedHosts").description("The client's allowed hosts"),
                fieldWithPath("_embedded.clientInstances.[].tlsEnabled").description("If client is must use TLS or not"),
                fieldWithPath("_embedded.clientInstances.[].resources").description("The resource ids of the resources the client should have access to"),
                fieldWithPath("_embedded.clientInstances.[].authorities").description("The list of authorities granted to the client"),
                fieldWithPath("_embedded.clientInstances.[].includeUserRoles").description("Include user's roles in the token info"),
                fieldWithPath("_embedded.clientInstances.[].includeUserAuthorities").description("Include user's authorities in the token info"),
                fieldWithPath("_embedded.clientInstances.[].credentials.type").optional().description("The client's credential type (for now, `secret`)"),
                fieldWithPath("_embedded.clientInstances.[].credentials.secret").optional().description("The client secret (if credential type is `secret`)").optional()
        );
    }

    @Test
    public void getClientInstancesByDomainAndClient() throws Exception {
        ClientInstance instance = newClientInstance();
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/admin/repo/clientInstances/search/findByDomainIdAndClientId")
                        .param("domainId", instance.getDomainId())
                        .param("clientId", instance.getClientId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.client_instance.read"))
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, requestParameters(
                                parameterWithName("domainId").description("Domain ID of the client instance"),
                                parameterWithName("clientId").description("The client's ID")
                        ), relaxedResponseFields(
                                clientInstanceListFields()
                        ))
                );
    }


    @Test
    public void updateClientInstanceById() throws Exception {
        when(domainIdentityConfigurationService.findByDomainId(DOMAIN_ID)).thenReturn(Optional.of(configuration));
        final boolean tlsEnabled = false;
        ClientInstance instance = newClientInstance();
        mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/admin/repo/clientInstances/{clientInstanceId}", instance.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.client_instance.update"))
                        .content(
                                objectMapper.createObjectNode()
                                        .put("tlsEnabled", tlsEnabled)
                                        .toString()
                        )
        ).andExpect(status().isNoContent())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                clientInstanceIdParameter()
                        ))
                );
        assertThat(clientInstanceRepository.findById(instance.getId()).get().isTlsEnabled(), equalTo(tlsEnabled));
    }

    @Test
    public void deleteClientById() throws Exception {
        ClientInstance instance = newClientInstance();
        mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/admin/repo/clientInstances/{clientInstanceId}", instance.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.client_instance.delete"))
        ).andExpect(status().isNoContent())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                clientInstanceIdParameter()
                        ))
                );
        assertThat(clientInstanceRepository.existsById(instance.getId()), equalTo(false));
    }
}
