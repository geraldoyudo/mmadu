package com.mmadu.security.providers.domainparsers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathVariableDomainExtractorTest {
    private final PathVariableDomainExtractor domainExtractor = new PathVariableDomainExtractor();

    @Mock
    private HttpServletRequest request;

    @Test
    void givenPathWithDomainIdWhenExtractDomainShouldReturnDomain() {
        when(request.getRequestURI()).thenReturn("http://localhost/something/else/domain/1111");
        when(request.getContextPath()).thenReturn("/");
        assertEquals("1111", domainExtractor.extractDomainIdFromRequest(request).orElse(""));
    }

    @Test
    void givenPathWithDomainIdWhenCustomExtractDomainShouldReturnDomain() {
        when(request.getRequestURI()).thenReturn("http://localhost/something/else/domainId/1111");
        when(request.getContextPath()).thenReturn("/");
        domainExtractor.setDomainKeys(List.of("tenant", "domainId"));
        assertEquals("1111", domainExtractor.extractDomainIdFromRequest(request).orElse(""));
    }

    @Test
    void givenPathWithoutDomainIdWhenExtractDomainShouldReturnDomain() {
        when(request.getRequestURI()).thenReturn("http://localhost/something/else/domain/");
        when(request.getContextPath()).thenReturn("/");
        assertTrue(domainExtractor.extractDomainIdFromRequest(request).isEmpty());
    }
}