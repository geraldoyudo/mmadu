package com.mmadu.service.documentation;

import static com.mmadu.service.models.AuthenticationStatus.AUTHENTICATED;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.service.models.AuthenticateRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.generate.RestDocumentationGenerator;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthenticationDocumentation {
    private static final String ROOT_DOC_FOLDER = "../docs/apis/snippets";
    private static final String DOCUMENTATION_NAME = "{class-name}/{method-name}/{step}/";
    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(ROOT_DOC_FOLDER);

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(document(DOCUMENTATION_NAME,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()))).build();
    }

    @Test
    public void authentication() throws Exception {
        this.mockMvc.perform(post("/authenticate")
            .contentType(MediaType.APPLICATION_JSON).content(objectMapper
                        .writerWithDefaultPrettyPrinter().writeValueAsString(
                                AuthenticateRequest.builder().username("user").password("password").domain("test")
                                .build()
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(AUTHENTICATED.name()))
                .andDo(document(DOCUMENTATION_NAME, requestFields(
                        fieldWithPath("username").description("The user identification"),
                        fieldWithPath("domain").description("The user authentication domain id"),
                        fieldWithPath("password").description("The user's password")
                ), responseFields(
                        fieldWithPath("status").description("The authentication status. One of the following: "
                                + "AUTHENTICATED, USERNAME_INVALID, PASSWORD_INVALID")
                )));
    }
}
