package com.mmadu.service.documentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mmadu.service.config.MongoInitializationConfig;
import com.mmadu.service.entities.AppDomain;
import com.mmadu.service.entities.AppUser;
import com.mmadu.service.repositories.AppDomainRepository;
import com.mmadu.service.repositories.AppUserRepository;
import com.mmadu.service.utilities.TokenGeneratorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@Import({
        MongoInitializationConfig.class,
        AbstractDocumentation.SerializationConfig.class
})
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AbstractDocumentation {
    public static final String ROOT_DOC_FOLDER = "../docs/apis/snippets";
    public static final String DOCUMENTATION_NAME = "{class-name}/{method-name}/{step}/";
    public static final String TEST_USER_ID = "12345ABCDEF";
    public static final String USER_DOMAIN_ID = "test-app";
    public static final String USER_PASSWORD = "my-password";
    public static final String USERNAME = "test-user";
    private static final String DOMAIN_NAME = "test";
    public static final String DOMAIN_TOKEN = "1234";
    public static final String USER_EXTERNAL_ID = "123453432";
    protected final String ADMIN_TOKEN = "eyJraWQiOiIxMjMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI1ZWUzNzhhZDQ3NDg5MTI5Y2M0OWIzYjAiLCJyb2xlcyI6W10sImlzcyI6Im1tYWR1LmNvbSIsImdyb3VwcyI6WyJ0ZXN0Iiwic2FtcGxlIl0sImF1dGhvcml0aWVzIjpbXSwiY2xpZW50X2lkIjoiMjJlNjViNzItOTIzNC00MjgxLTlkNzMtMzIzMDA4OWQ0OWE3IiwiZG9tYWluX2lkIjoiMCIsImF1ZCI6InRlc3QiLCJuYmYiOjE1OTE5NjU4OTIsInVzZXJfaWQiOiIxMTExMTExMTEiLCJzY29wZSI6InZpZXcgZWRpdCIsImV4cCI6Mjk5MTk2NjE5MiwiaWF0IjoxNTkxOTY1ODkyLCJqdGkiOiJmNWJmNzVhNi0wNGEwLTQyZjctYTFlMC01ODNlMjljZGU4NmMifQ.EgjaDmX03BYWbBdnFx4rYLTlT3wnRqnILd-pLWZLrUPZ48llyuzPoB2dJ3QcNSuzxb9koOS55513nzpKekOAkcDuA3XP7OTxw_4X5rar7xQiA3gEnQ1RAgUcUCOXGmlzl5f9XQsdHtY-WxMuh-qgdELqH8fkb4p0HcAHOOdhKOivSoIGu1uGBrbmT8RFUcAti1mmUzDJM0RFn0JZc7IULizoaibEh-mGNuBn0AN2ZhK1xRM-tbKIOZBp5_wVY1YcGc7M1bO-VeCmg2dWilZC9_9GT2X2t4E1vXoz1a4OkiBZx27GhZwJCSWnrve5OwRPf4ONTV7B0FZqJxFP3yQTIg";
    private TokenGeneratorUtils tokenGenerator;

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected AppUserRepository appUserRepository;
    @Autowired
    protected AppDomainRepository appDomainRepository;

    protected MockMvc mockMvc;

    @BeforeEach
    void initializeTest(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) throws Exception {
        this.tokenGenerator = TokenGeneratorUtils.getInstance();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document(DOCUMENTATION_NAME,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()))).build();
    }

    protected final String objectToString(Object object) throws JsonProcessingException {
        return objectMapper
                .writerWithDefaultPrettyPrinter().writeValueAsString(
                        object
                );
    }

    protected AppUser createAppUserWithConstantId() {
        AppUser user = new AppUser();
        user.setDomainId(USER_DOMAIN_ID);
        user.setPassword(USER_PASSWORD);
        user.setUsername(USERNAME);
        user.setId(TEST_USER_ID);
        user.setExternalId(USER_EXTERNAL_ID);
        user.set("country", "Nigeria");
        user.set("favourite-colour", "blue");
        return user;
    }

    protected AppUser createAppUserWithIndex(int index) {
        AppUser user = new AppUser();
        user.setDomainId(USER_DOMAIN_ID);
        user.setPassword(USER_PASSWORD + index);
        user.setUsername(USERNAME + index);
        user.setId(TEST_USER_ID + index);
        user.setExternalId(USER_EXTERNAL_ID);
        user.set("country", "Nigeria");
        user.set("favourite-color", "blue");
        return user;
    }

    protected List<AppUser> createMultipleUsers(int size) {
        List<AppUser> appUsers = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            appUsers.add(createAppUserWithIndex(i));
        }
        return appUsers;
    }

    protected AppDomain createDomain() {
        AppDomain domain = new AppDomain();
        domain.setName(DOMAIN_NAME);
        domain.setId(USER_DOMAIN_ID);
        return domain;
    }

    protected String authorization(String... authorities) throws Exception {
        return "Bearer " + tokenGenerator.generateTokenWithAuthorities(authorities);
    }

    @Configuration
    public static class SerializationConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        }
    }
}
