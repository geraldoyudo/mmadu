package com.mmadu.security.providers.domainparsers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DomainParserImpl implements DomainParser {
    private List<DomainExtractor> extractors = Collections.emptyList();

    public void setExtractors(List<DomainExtractor> extractors) {
        this.extractors = extractors;
    }

    @Override
    public Optional<String> parseDomain(Object request) {
        return extractors.stream()
                .flatMap(extractor -> extractor.extractDomainId(request).stream())
                .findFirst();
    }
}
