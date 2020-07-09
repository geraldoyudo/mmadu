package com.mmadu.registration.providers;

import com.mmadu.registration.entities.DomainFlowConfiguration;
import com.mmadu.registration.repositories.DomainFlowConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DomainServiceImpl implements DomainService {
    private DomainFlowConfigurationRepository domainFlowConfigurationRepository;

    @Autowired
    public void setDomainFlowConfigurationRepository(DomainFlowConfigurationRepository domainFlowConfigurationRepository) {
        this.domainFlowConfigurationRepository = domainFlowConfigurationRepository;
    }

    @Override
    public List<String> getDomainIds() {
        return domainFlowConfigurationRepository.findAll()
                .stream()
                .map(DomainFlowConfiguration::getDomainId)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public boolean domainExists(String domain) {
        return domainFlowConfigurationRepository.existsByDomainId(domain);
    }
}
