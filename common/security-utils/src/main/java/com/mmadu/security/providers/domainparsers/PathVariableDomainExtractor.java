package com.mmadu.security.providers.domainparsers;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathVariableDomainExtractor implements HttpRequestDomainExtractor {
    private List<Pattern> domainPathVariablePattern = List.of(
            Pattern.compile("domain/([a-zA-Z0-9]*)[/]?")
    );

    public void setDomainKeys(List<String> domainKeys) {
        this.domainPathVariablePattern = domainKeys.stream()
                .map(key -> String.format("%s/([a-zA-Z0-9]*)[/]?", key))
                .map(Pattern::compile)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<String> extractDomainIdFromRequest(HttpServletRequest request) {
        return domainPathVariablePattern.stream()
                .flatMap(paramName -> extractFromPathVariableWithPattern(paramName, request))
                .findFirst();
    }

    private Stream<String> extractFromPathVariableWithPattern(Pattern domainParamPattern, HttpServletRequest request) {
        String path = request.getServletPath();
        Matcher matcher = domainParamPattern.matcher(path);
        if (!matcher.find()) {
            return Stream.empty();
        }
        String domainId = matcher.group(1);
        if (StringUtils.isEmpty(domainId)) {
            return Stream.empty();
        }
        return Stream.of(domainId);
    }
}
