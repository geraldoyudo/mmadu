package com.mmadu.identity.utils;

import com.mmadu.identity.entities.ClientCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ClientCredentialsTypeRegistry {
    private List<ClientCredentialsRegistration> registrationList = Collections.emptyList();

    @Autowired(required = false)
    public void setRegistrationList(List<ClientCredentialsRegistration> registrationList) {
        this.registrationList = registrationList;
    }

    private Map<String, Class<? extends ClientCredentials>> typeMap = Collections.emptyMap();

    @PostConstruct
    public void init() {
        typeMap = registrationList.stream()
                .collect(Collectors.toMap(ClientCredentialsRegistration::getName, ClientCredentialsRegistration::getType));
    }

    public Optional<Class<? extends ClientCredentials>> getCredentialType(String credentialType) {
        return Optional.ofNullable(typeMap.get(credentialType));
    }

    public Optional<String> getTypeName(ClientCredentials credentials){
        Class<? extends ClientCredentials> clazz = credentials.getClass();
        return typeMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(clazz))
                .map(Map.Entry::getKey)
                .findFirst();
    }
}
