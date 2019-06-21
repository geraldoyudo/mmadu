package com.mmadu.registration.providers;

import java.util.List;

public interface DomainService {

    List<String> getDomainIds();

    boolean domainExists(String domain);
}
