package com.mmadu.identity.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mmadu.identity.models.domain.Domain;
import com.mmadu.identity.services.domain.DomainService;
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

import java.util.Optional;

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
    public static final String DOMAIN_ID = "1";
    protected final String ADMIN_TOKEN = "eyJraWQiOiIxMjMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI1ZWUzNzhhZDQ3NDg5MTI5Y2M0OWIzYjAiLCJyb2xlcyI6W10sImlzcyI6Im1tYWR1LmNvbSIsImdyb3VwcyI6WyJ0ZXN0Iiwic2FtcGxlIl0sImF1dGhvcml0aWVzIjpbXSwiY2xpZW50X2lkIjoiMjJlNjViNzItOTIzNC00MjgxLTlkNzMtMzIzMDA4OWQ0OWE3IiwiZG9tYWluX2lkIjoiMCIsImF1ZCI6InRlc3QiLCJuYmYiOjE1OTE5NjU4OTIsInVzZXJfaWQiOiIxMTExMTExMTEiLCJzY29wZSI6InZpZXcgZWRpdCIsImV4cCI6Mjk5MTk2NjE5MiwiaWF0IjoxNTkxOTY1ODkyLCJqdGkiOiJmNWJmNzVhNi0wNGEwLTQyZjctYTFlMC01ODNlMjljZGU4NmMifQ.EgjaDmX03BYWbBdnFx4rYLTlT3wnRqnILd-pLWZLrUPZ48llyuzPoB2dJ3QcNSuzxb9koOS55513nzpKekOAkcDuA3XP7OTxw_4X5rar7xQiA3gEnQ1RAgUcUCOXGmlzl5f9XQsdHtY-WxMuh-qgdELqH8fkb4p0HcAHOOdhKOivSoIGu1uGBrbmT8RFUcAti1mmUzDJM0RFn0JZc7IULizoaibEh-mGNuBn0AN2ZhK1xRM-tbKIOZBp5_wVY1YcGc7M1bO-VeCmg2dWilZC9_9GT2X2t4E1vXoz1a4OkiBZx27GhZwJCSWnrve5OwRPf4ONTV7B0FZqJxFP3yQTIg";
    public static final String DOMAIN_AUTH_TOKEN_FIELD = "domain-auth-token";

    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private DomainService domainService;

    protected MockMvc mockMvc;

    @BeforeEach
    void initializeTest(WebApplicationContext context,
                        RestDocumentationContextProvider restDocumentation) {
        doReturn(Optional.of(new Domain("new-domain")))
                .when(domainService).findById(DOMAIN_ID);
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
