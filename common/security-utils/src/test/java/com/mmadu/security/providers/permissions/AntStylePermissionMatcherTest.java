package com.mmadu.security.providers.permissions;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AntStylePermissionMatcherTest {
    private AntStylePermissionMatcher matcher = new AntStylePermissionMatcher();

    @ParameterizedTest
    @CsvSource({
            "1111.um.view,1111.um.view,true",
            "1111.um.view,1111.um.edit,false",
            "*.um.view,1111.um.edit,false",
            "*.um.view,1111.um.view,true",
            "*.**,1111.um.view,true",
            "*.**,1111.xm,true",
            "*.**,1111.xm.view.some,true",
    })
    void testMatching(String pattern, String permission, boolean match) {
        assertEquals(match, matcher.matchesPermission(pattern, permission));
    }
}