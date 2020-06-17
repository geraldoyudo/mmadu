package com.mmadu.identity.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CredentialCreationDocumentation extends AbstractDocumentation {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DomainIdentityConfigurationService domainIdentityConfigurationService;
    @Mock
    private DomainIdentityConfiguration configuration;

    @Test
    void createNewClient() throws Exception {
        when(domainIdentityConfigurationService.findByDomainId(DOMAIN_ID)).thenReturn(Optional.of(configuration));
        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/admin/credentials/{domainId}", DOMAIN_ID)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + ADMIN_TOKEN)
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
}
