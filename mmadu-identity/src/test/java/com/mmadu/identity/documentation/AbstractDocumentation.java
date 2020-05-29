package com.mmadu.identity.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mmadu.security.DomainTokenChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@Import({
        AbstractDocumentation.SerializationConfig.class
})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
public abstract class AbstractDocumentation {
    public static final String ROOT_DOC_FOLDER = "../docs/apis/snippets";
    public static final String DOCUMENTATION_NAME = "{class-name}/{method-name}/{step}/";
    public final String ADMIN_TOKEN = "2222";
    public static final String DOMAIN_AUTH_TOKEN_FIELD = "domain-auth-token";

    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private DomainTokenChecker domainTokenChecker;

    protected MockMvc mockMvc;

    @BeforeEach
    void initializeTest(WebApplicationContext context,
                        RestDocumentationContextProvider restDocumentation) {
        doReturn(true).when(domainTokenChecker)
                .checkIfTokenMatchesDomainToken(ADMIN_TOKEN, "admin");
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document(DOCUMENTATION_NAME,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()))).build();
    }

    @Configuration
    public static class SerializationConfig {
        public SerializationConfig(ObjectMapper objectMapper) {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
    }
}
