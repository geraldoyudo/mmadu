package com.mmadu.service.security.domainidextractors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;

import com.mmadu.service.models.DomainIdObject;
import com.mmadu.service.repositories.AppUserRepository;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AppUserIdDomainIdExtractorTest {
    private static final String USER_ID = "3333";
    private static final String DOMAIN_ID = "1234";

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private final AppUserIdDomainIdExtractor domainIdExtractor = new AppUserIdDomainIdExtractor();

    @Test
    public void givenDomainIdForUserIdWhenExtractDomainIdThenReturnCorrectDomainId() {
        doReturn(Optional.of(new DomainIdObject(DOMAIN_ID))).when(appUserRepository).findDomainIdForUser(USER_ID);
        assertThat(domainIdExtractor.extractDomainId(USER_ID).get(), equalTo(DOMAIN_ID));
    }

    @Test
    public void givenNoDomainIdForUserIdWhenExtractDomainIdThenReturnNothing() {
        doReturn(Optional.empty()).when(appUserRepository).findDomainIdForUser(USER_ID);
        assertThat(domainIdExtractor.extractDomainId(USER_ID).isPresent(), equalTo(false));
    }

    @Test
    public void domain() {
        assertThat(domainIdExtractor.domain(), equalTo("user"));
    }
}