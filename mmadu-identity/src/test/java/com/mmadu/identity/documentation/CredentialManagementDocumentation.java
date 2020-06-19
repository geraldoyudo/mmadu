package com.mmadu.identity.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.entities.credentials.Credential;
import com.mmadu.identity.entities.credentials.RSACredentialData;
import com.mmadu.identity.repositories.CredentialRepository;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import com.nimbusds.jose.util.IOUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
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
    public static final String PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100a31b559ff90788f92436bb61ad0d528cf088a9190a97c7dbf98539409663d54d3fb4a089ccd77ca49165d8d5a76b21b30fc733a558569647498b182dc4a06ea7fd1022b761877c9776d0db5107b8f3e0c67ba0f101315be5989d0a33c6a431a3de07c071457672c6266a1e89d079222c42031ca7c3b563a913c41eee45d20ddaf58a92014adbc4bbe135c055c604380b649b3178540fc4a0c2f7c46aa90f62422d5ae621332bacf6771f2319ae03936fdaf346abdc599e3b63cae4c0d4d8ec5832f1c61b5e370005c3aed880130513970b79de6d5734aa11dcf8fb866ea9d0cfafd55d0c4f27a13763f1d193ca402c5c8a65e198a7500b3e9928552e19a40d3d0203010001";
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DomainIdentityConfigurationService domainIdentityConfigurationService;
    @Mock
    private DomainIdentityConfiguration configuration;
    @Autowired
    private CredentialRepository credentialRepository;

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
        Credential credential = createAndSaveCredential();
        mockMvc.perform(RestDocumentationRequestBuilders.get(
                "/admin/domains/{domainId}/credentials/{credentialId}/verificationKey",
                DOMAIN_ID, credential.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization("a.1.credential.read"))
        ).andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(PUBLIC_KEY))
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                parameterWithName("domainId").description("domain for the credentials to be crated in"),
                                parameterWithName("credentialId").description("the credential id")
                        ))
                );

    }

    private Credential createAndSaveCredential() throws Exception {
        Credential credential = new Credential();
        credential.setDomainId(DOMAIN_ID);
        RSACredentialData data = new RSACredentialData();
        data.setKeyData(
                IOUtils.readInputStreamToString(new ClassPathResource("/keys/key-data.json").getInputStream())
        );
        credential.setData(data);
        return credentialRepository.save(credential);
    }
}
