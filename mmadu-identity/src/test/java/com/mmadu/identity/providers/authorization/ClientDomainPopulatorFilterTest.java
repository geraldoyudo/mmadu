package com.mmadu.identity.providers.authorization;

import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.services.client.MmaduClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.mmadu.identity.providers.authorization.ClientDomainPopulatorFilter.MMADU_DOMAIN_COOKIE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ClientDomainPopulatorFilterTest {
    public static final String CLIENT_ID = "1234";
    public static final String DOMAIN_ID = "223223";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MmaduClientService mmaduClientService;
    @MockBean
    private MmaduClient client;

    @Test
    void whenOauthAuthorizeApiIsCalledWithClientIdShouldRedirectToLoginPageWithDomainAttributeSet() throws Exception {
        when(mmaduClientService.loadClientByIdentifier(CLIENT_ID)).thenReturn(Optional.of(client));
        when(client.getDomainId()).thenReturn(DOMAIN_ID);

        mockMvc.perform(
                get("/oauth/authorize")
                        .param("client_id", CLIENT_ID)
        ).andExpect(
                status().is3xxRedirection()
        ).andExpect(
                header().string("location", "http://localhost/login")
        ).andExpect(
                cookie().value(MMADU_DOMAIN_COOKIE, DOMAIN_ID)
        );
    }
}