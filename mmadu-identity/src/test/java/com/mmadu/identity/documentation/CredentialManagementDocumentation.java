package com.mmadu.identity.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.models.security.CredentialGenerationRequest;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import com.mmadu.identity.services.security.CredentialService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CredentialManagementDocumentation extends AbstractDocumentation {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DomainIdentityConfigurationService domainIdentityConfigurationService;
    @Mock
    private DomainIdentityConfiguration configuration;
    @Autowired
    private CredentialService credentialService;

    @Test
    void createNewCredential() throws Exception {
        when(domainIdentityConfigurationService.findByDomainId(DOMAIN_ID)).thenReturn(Optional.of(configuration));
        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/admin/domains/{domainId}/credentials", DOMAIN_ID)
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.credential.create"))
                        .accept(MediaType.TEXT_PLAIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newCredentialsRequest())
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                parameterWithName("domainId").description("domain for the credentials to be crated in")
                        ))
                );
    }

    private String newCredentialsRequest() {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("type", "rsa");
        return node.toPrettyString();
    }

    @Test
    void getCredentialVerificationKey() throws Exception {
        when(domainIdentityConfigurationService.findByDomainId(DOMAIN_ID)).thenReturn(Optional.of(configuration));
        String credentialId = createAndSaveCredential();
        mockMvc.perform(RestDocumentationRequestBuilders.get(
                "/admin/domains/{domainId}/credentials/{credentialId}/verificationKey",
                DOMAIN_ID, credentialId)
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.credential.read"))
        ).andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                parameterWithName("domainId").description("domain for the credentials to be crated in"),
                                parameterWithName("credentialId").description("the credential id")
                        ))
                );

    }

    private String createAndSaveCredential() throws Exception {
        CredentialGenerationRequest request = new CredentialGenerationRequest();
        request.setType("rsa");
        return credentialService.generateCredentialForDomain(DOMAIN_ID, request);
    }
}
