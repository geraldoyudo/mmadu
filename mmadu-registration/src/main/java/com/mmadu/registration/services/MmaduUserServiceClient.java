package com.mmadu.registration.services;

import java.util.Map;

public interface MmaduUserServiceClient {

    void addUsers(String domainId, Map<String, Object> user);
}
