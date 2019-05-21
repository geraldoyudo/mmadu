package com.mmadu.service.security.domainidextractors;

import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class TransparentDomainIdExtractorTest {
    private final DomainIdExtractor domainIdExtractor = new TransparentDomainIdExtractor();
    private final String DOMAIN_ID = "1234";

    @Test
    public void extractDomainId() {
        assertThat(domainIdExtractor.extractDomainId(DOMAIN_ID).get(), equalTo(DOMAIN_ID));
    }

    @Test
    public void domain() {
        assertThat(domainIdExtractor.domain(), equalTo("domain"));
    }
}