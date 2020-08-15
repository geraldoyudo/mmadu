package com.mmadu.security.providers.domainparsers;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface TenantExtractor {

    Optional<String> getDomain(HttpServletRequest request);
}
