package com.mmadu.security.providers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class MmaduSecurityExpressionRootTest {
    private static final List<String> AUTHORITIES_LIST = List.of(
            "a.111.uv.view", "a.111.xy.*", "r.111.uv.mon", "a.2222.ab.*", "r.2222.ab.admin"
    );
    @Mock
    private Authentication authentication;

    @InjectMocks
    private TestExpressionRoot root;

    @BeforeEach
    void setUp() {
        Collection<? extends GrantedAuthority> authorities = AUTHORITIES_LIST.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        doReturn(authorities).when(authentication).getAuthorities();
        root.setDomainId("111");
    }

    @ParameterizedTest
    @CsvSource({
            "uv.view,true",
            "ab.view,false",
            "xy.edit,true",
    })
    void hasAuthority(String authority, boolean hasAuthority) {
        assertEquals(hasAuthority, root.hasAuthority(authority));
    }

    @ParameterizedTest
    @CsvSource({
            "uv.view;xy.edit,true",
            "xy.view;xy.edit,true",
            "uv.edit;ab.place,false"
    })
    void hasAnyAuthority(String authorities, boolean hasAuthority) {
        assertEquals(hasAuthority, root.hasAnyAuthority(authorities.split("[;]")));
    }

    @ParameterizedTest
    @CsvSource({
            "uv.mon,true",
            "ab.admin,false"
    })
    void hasRole(String role, boolean hasRole) {
        assertEquals(hasRole, root.hasRole(role));
    }

    @ParameterizedTest
    @CsvSource({
            "uv.mon;ab.admin,true",
            "uv.sit;ab.admin,false",
    })
    void hasAnyRole(String roles, boolean hasRole) {
        assertEquals(hasRole, root.hasAnyRole(roles.split("[;]")));
    }

    static class TestExpressionRoot extends MmaduSecurityExpressionRoot {
        public TestExpressionRoot(Authentication authentication) {
            super(authentication);
        }
    }
}