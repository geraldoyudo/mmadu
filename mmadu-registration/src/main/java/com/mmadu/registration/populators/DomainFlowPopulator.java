package com.mmadu.registration.populators;

import com.mmadu.registration.config.DomainFlowConfiguration;
import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.models.RegistrationFieldModifiedEvent;
import com.mmadu.registration.repositories.FieldRepository;
import com.mmadu.registration.repositories.FieldTypeRepository;
import com.mmadu.registration.repositories.RegistrationProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DomainFlowPopulator implements Populator {
    private DomainFlowConfiguration domainFlowConfiguration;
    private FieldTypeRepository fieldTypeRepository;
    private FieldRepository fieldRepository;
    private RegistrationProfileRepository registrationProfileRepository;
    private ApplicationEventPublisher publisher;

    @Autowired
    public void setDomainFlowConfiguration(DomainFlowConfiguration domainFlowConfiguration) {
        this.domainFlowConfiguration = domainFlowConfiguration;
    }

    @Autowired
    public void setFieldRepository(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    @Autowired
    public void setFieldTypeRepository(FieldTypeRepository fieldTypeRepository) {
        this.fieldTypeRepository = fieldTypeRepository;
    }

    @Autowired
    public void setRegistrationProfileRepository(RegistrationProfileRepository registrationProfileRepository) {
        this.registrationProfileRepository = registrationProfileRepository;
    }

    @Autowired
    public void setPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    @PostConstruct
    public void populate() {
        initializeDomainEnvironment(domainFlowConfiguration);
    }

    public void initializeDomainEnvironment(DomainFlowConfiguration configuration) {
        List<DomainFlowConfiguration.FieldTypeItem> newFieldTypes = Optional.ofNullable(configuration.getFieldTypes()).orElse(Collections.emptyList())
                .stream()
                .filter(ft -> !fieldTypeRepository.existsById(ft.getId()))
                .collect(Collectors.toList());
        if (!newFieldTypes.isEmpty()) {
            initializeFieldTypes(newFieldTypes);
        }

        List<DomainFlowConfiguration.DomainItem> domains = Optional.ofNullable(configuration.getDomains()).orElse(Collections.emptyList())
                .stream()
                .filter(d -> !registrationProfileRepository.existsByDomainId(d.getDomainId()))
                .collect(Collectors.toList());
        if (!domains.isEmpty()) {
            initializeDomains(domains);
        }
    }

    private void initializeFieldTypes(List<DomainFlowConfiguration.FieldTypeItem> fieldTypes) {
        List<FieldType> appFieldTypes = fieldTypes.stream()
                .map(DomainFlowConfiguration.FieldTypeItem::toEntity)
                .collect(Collectors.toList());
        fieldTypeRepository.saveAll(appFieldTypes);
    }

    private void initializeDomains(List<DomainFlowConfiguration.DomainItem> domainItems) {
        domainItems.forEach(this::initializeDomain);
    }

    private void initializeDomain(DomainFlowConfiguration.DomainItem domainItem) {
        Optional.ofNullable(domainItem.getRegistrationProfile())
                .ifPresent(p -> this.registerProfile(domainItem.getDomainId(), p));
        List<DomainFlowConfiguration.FieldItem> fieldItems = Optional.ofNullable(domainItem.getFields())
                .orElse(Collections.emptyList());
        if (!fieldItems.isEmpty()) {
            registerFields(domainItem.getDomainId(), fieldItems);
        }
        publisher.publishEvent(new RegistrationFieldModifiedEvent(domainItem.getDomainId()));
    }

    private void registerProfile(String domainId, DomainFlowConfiguration.RegistrationProfileItem profileItem) {
        registrationProfileRepository.save(profileItem.toEntity(domainId));
    }

    private void registerFields(String domainId, List<DomainFlowConfiguration.FieldItem> fieldItems) {
        List<Field> fields = fieldItems.stream()
                .map(f -> f.toEntity(domainId))
                .collect(Collectors.toList());
        fieldRepository.saveAll(fields);
    }
}
