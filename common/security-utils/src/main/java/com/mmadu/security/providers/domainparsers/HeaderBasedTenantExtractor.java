package com.mmadu.security.providers.domainparsers;

import com.mmadu.security.providers.utils.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class HeaderBasedTenantExtractor implements TenantExtractor {

    @Override
    public Optional<String> getDomain(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(RequestUtils.DOMAIN_HEADER));
    }
}
