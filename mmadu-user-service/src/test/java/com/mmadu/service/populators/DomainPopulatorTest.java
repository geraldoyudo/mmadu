package com.mmadu.service.populators;

import com.mmadu.service.repositories.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;

@SpringBootTest
@ActiveProfiles("populated")
@Import(DomainPopulatorTest.TestEventListener.class)
class DomainPopulatorTest {
    @MockBean
    private Handler handler;

    private final CountDownLatch latch = new CountDownLatch(1);
    @Captor
    private ArgumentCaptor<List<String>> domainIdCaptor;
    @Autowired
    private AppDomainRepository appDomainRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleAuthorityRepository roleAuthorityRepository;
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    void ensureItemsWerePopulatedSuccessfully() throws Exception {
        doAnswer(iom -> {
            latch.countDown();
            return null;
        }).when(handler).handle(domainIdCaptor.capture());
        latch.await(3, TimeUnit.SECONDS);
        assertAll(
                () -> assertEquals(1, appDomainRepository.count()),
                () -> assertEquals(1, appUserRepository.count()),
                () -> assertEquals(2, authorityRepository.count()),
                () -> assertEquals(1, roleRepository.count()),
                () -> assertEquals(2, groupRepository.count()),
                () -> assertEquals(1, userAuthorityRepository.count()),
                () -> assertEquals(1, userGroupRepository.count()),
                () -> assertEquals(1, userRoleRepository.count()),
                () -> assertEquals(1, roleAuthorityRepository.count())
        );
    }

    @Component
    public static class TestEventListener {
        private Handler handler;

        @Autowired
        public void setHandler(Handler handler) {
            this.handler = handler;
        }

        @EventListener(DomainPopulatedEvent.class)
        public void listen(DomainPopulatedEvent event) {
            handler.handle(event.getDomainIds());
        }
    }

    public interface Handler {
        void handle(List<String> domainIds);
    }
}