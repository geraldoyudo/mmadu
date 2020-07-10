package com.mmadu.registration.services;

import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.exceptions.UserFormValidationException;
import com.mmadu.registration.models.UserForm;
import com.mmadu.registration.models.UserModel;
import com.mmadu.registration.providers.UserFormConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static com.mmadu.registration.utils.EntityUtils.DOMAIN_CODE;
import static com.mmadu.registration.utils.EntityUtils.DOMAIN_ID;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        RegistrationServiceImpl.class,
        RegistrationServiceImplTest.Config.class
})
public class RegistrationServiceImplTest {
    @MockBean
    private RegistrationProfileService registrationProfileService;
    @Autowired
    private RegistrationService registrationService;
    @MockBean
    private MmaduUserServiceClient mmaduUserServiceClient;
    @Captor
    private ArgumentCaptor<Map<String, Object>> userCaptor;

    @Test
    public void givenUserNameAbsentWhenRegisterUserThenThrowUserFormValidationException() {
        UserForm userForm = new UserForm();
        userForm.set("password", "password");
        Exception ex = assertThrows(UserFormValidationException.class,
                () -> registrationService.registerUser(DOMAIN_ID, DOMAIN_CODE, userForm));
        assertEquals("username cannot be empty", ex.getMessage());
    }

    @Test
    public void givenUserWhenRegisterUserThenAddDefaultRolesAndAuthorities() {
        UserForm userForm = new UserForm();
        userForm.set("username", "user");
        RegistrationProfile profile = getRegistrationProfile();
        doReturn(profile).when(registrationProfileService).getProfileForDomainAndCode(DOMAIN_ID, DOMAIN_CODE);
        registrationService.registerUser(DOMAIN_ID, DOMAIN_CODE, userForm);
        verify(mmaduUserServiceClient, times(1)).addUsers(eq(DOMAIN_ID), userCaptor.capture());
        Map<String, Object> userProperties = userCaptor.getValue();
        assertThat(userProperties.get("roles"), equalTo(profile.getDefaultRoles()));
        assertThat(userProperties.get("authorities"), equalTo(profile.getDefaultAuthorities()));
    }

    private RegistrationProfile getRegistrationProfile() {
        RegistrationProfile profile = new RegistrationProfile();
        profile.setDomainId(DOMAIN_ID);
        profile.setId("1");
        profile.setDefaultRedirectUrl("http://google.com");
        profile.setDefaultAuthorities(asList("manage-list"));
        profile.setDefaultRoles(asList("member"));
        return profile;
    }

    @Test
    public void givenNullPasswordWhenAddUserEnsurePasswordIsEmpty() {
        UserForm userForm = new UserForm();
        userForm.set("username", "user");
        RegistrationProfile profile = getRegistrationProfile();
        doReturn(profile).when(registrationProfileService).getProfileForDomainAndCode(DOMAIN_ID, DOMAIN_CODE);
        registrationService.registerUser(DOMAIN_ID, DOMAIN_CODE, userForm);
        verify(mmaduUserServiceClient, times(1)).addUsers(eq(DOMAIN_ID), userCaptor.capture());
        Map<String, Object> userProperties = userCaptor.getValue();
        assertThat(userProperties.get("password"), equalTo(""));
    }

    @Test
    public void givenNullUserFormThrowIllegalArgumentException() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> registrationService.registerUser(DOMAIN_ID, DOMAIN_CODE,null));
        assertEquals("user form cannot be null", ex.getMessage());
    }

    @TestConfiguration
    public static class Config {
        @Bean
        public UserFormConverter userFormConverter() {
            return (domainId, userForm) -> {
                Map<String, Object> returnValue = new HashMap<>();
                userForm.getProperties().entrySet()
                        .stream()
                        .forEach(entry -> returnValue.put(entry.getKey(), entry.getValue()));
                return new UserModel(returnValue);
            };
        }
    }
}