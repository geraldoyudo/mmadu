package com.mmadu.security.providers.domainparsers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class HeaderDomainExtractorTest {
    private final DomainExtractor domainExtractor = new HttpHeaderDomainExtractor();
    @Mock
    private HttpServletRequest request;

    @Test
    void extractDomain() {
        when(request.getHeader("domainId")).thenReturn("1111");
        assertEquals("1111", domainExtractor.extractDomainId(request).orElse(""));
    }
}