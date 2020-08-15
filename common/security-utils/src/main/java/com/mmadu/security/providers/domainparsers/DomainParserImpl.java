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
        for (DomainExtractor extractor : extractors) {
            Optional<String> domain = extractor.extractDomainId(request);
            if (domain.isPresent()) {
                return domain;
            }
        }
        return Optional.empty();
    }
}
