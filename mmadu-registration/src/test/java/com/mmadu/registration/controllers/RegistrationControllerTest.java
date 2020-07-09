package com.mmadu.registration.controllers;

import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.models.UserForm;
import com.mmadu.registration.providers.UserFormValidator;
import com.mmadu.registration.providers.UserFormValidatorFactory;
import com.mmadu.registration.services.RegistrationProfileService;
import com.mmadu.registration.services.RegistrationService;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Errors;

import java.util.Arrays;

import static com.mmadu.registration.utils.EntityUtils.createRegistrationProfile;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {
        RegistrationController.class
})
@EnableAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class,
        SecurityFilterAutoConfiguration.class
})
@AutoConfigureMockMvc
@TestPropertySource(properties = "mmadu.domain.api-security-enabled=false")
public class RegistrationControllerTest {
    public static final String PROFILE_ID = "1";
    public static final String DOMAIN_ID = "1";
    public static final String DOMAIN_CODE = "1234";
    public static final String REDIRECT_URL = "http://google.com";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RegistrationProfileService registrationProfileService;
    @MockBean
    private RegistrationService registrationService;
    @MockBean
    private UserFormValidatorFactory userFormValidatorFactory;
    @MockBean
    private UserFormValidator userFormValidator;

    private RegistrationProfile profile = createRegistrationProfile(PROFILE_ID, DOMAIN_ID);

    @BeforeEach
    public void setUp() {
        doReturn(profile).when(registrationProfileService).getProfileForDomainAndCode(DOMAIN_ID, DOMAIN_CODE);
        doReturn(userFormValidator).when(userFormValidatorFactory).createValidatorForDomain(anyString());
        doReturn(true).when(userFormValidator).supports(eq(UserForm.class));
    }

    @Test
    public void whenGetRegisterApiCalledShouldReturnRegisterPage() throws Exception {

        mockMvc.perform(get("/{domainId}/register", DOMAIN_ID)
        ).andExpect(
                status().isOk()
        ).andExpect(
                view().name("register")
        ).andExpect(
                model().attribute("user", any(UserForm.class))
        ).andExpect(
                model().attribute("domainId", equalTo(DOMAIN_ID))
        );
    }

    @Test
    public void whenGetRegisterApiCalledWithRedirectUrlShouldReturnRegisterPageWithRedirectUrlAttribute()
            throws Exception {

        mockMvc.perform(get("/{domainId}/register", DOMAIN_ID)
                .param("redirectUrl", REDIRECT_URL)
        ).andExpect(
                status().isOk()
        ).andExpect(
                model().attribute("redirectUrl", equalTo(REDIRECT_URL))
        );
    }

    @Test
    public void whenPostRegisterShouldSendRedirectToRedirectUrl() throws Exception {
        mockMvc.perform(post("/{domainId}/register", DOMAIN_ID)
                .param("redirectUrl", REDIRECT_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("username", "user"),
                        new BasicNameValuePair("password", "password")
                ))))
        ).andExpect(
                status().is(302)
        ).andExpect(
                view().name("redirect:" + REDIRECT_URL)
        );
    }

    @Test
    public void givenValidationErrorsWhenPostShouldReturnRegisterWithErrorAndOtherPreviousProperties()
            throws Exception {
        String errorMessage = "error-message";
        doAnswer(invocationOnMock -> {
            Errors errors = invocationOnMock.getArgument(1);
            errors.reject(errorMessage);
            return null;
        }).when(userFormValidator).validate(ArgumentMatchers.any(), ArgumentMatchers.any(Errors.class));

        mockMvc.perform(post("/{domainId}/register", DOMAIN_ID)
                .param("redirectUrl", REDIRECT_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("username", "user"),
                        new BasicNameValuePair("password", "password")
                ))))
        ).andExpect(
                status().isOk()
        ).andExpect(
                view().name("register")
        ).andExpect(
                model().attribute("user", any(UserForm.class))
        ).andExpect(
                model().attribute("domainId", equalTo(DOMAIN_ID))
        );

    }
}