package com.mmadu.registration.providers;

import com.mmadu.registration.repositories.RegistrationProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DomainServiceImpl implements DomainService {
    @Autowired
    private RegistrationProfileRepository registrationProfileRepository;

    @Override
    public List<String> getDomainIds() {
        return registrationProfileRepository.findAll()
                .stream()
                .map(field -> field.getDomainId())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public boolean domainExists(String domain) {
        return registrationProfileRepository.findByDomainId(domain).isPresent();
    }
}
