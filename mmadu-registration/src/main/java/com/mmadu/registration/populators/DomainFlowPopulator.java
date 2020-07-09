package com.mmadu.registration.populators;

import com.mmadu.registration.config.DomainFlowConfigurationList;
import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.entities.RegistrationProfile;
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
    private DomainFlowConfigurationList domainFlowConfigurationList;
    private FieldTypeRepository fieldTypeRepository;
    private FieldRepository fieldRepository;
    private RegistrationProfileRepository registrationProfileRepository;
    private ApplicationEventPublisher publisher;

    @Autowired
    public void setDomainFlowConfigurationList(DomainFlowConfigurationList domainFlowConfigurationList) {
        this.domainFlowConfigurationList = domainFlowConfigurationList;
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
        initializeDomainEnvironment(domainFlowConfigurationList);
    }

    public void initializeDomainEnvironment(DomainFlowConfigurationList configuration) {
        List<DomainFlowConfigurationList.FieldTypeItem> newFieldTypes = Optional.ofNullable(configuration.getFieldTypes()).orElse(Collections.emptyList())
                .stream()
                .filter(ft -> !fieldTypeRepository.existsById(ft.getId()))
                .collect(Collectors.toList());
        if (!newFieldTypes.isEmpty()) {
            initializeFieldTypes(newFieldTypes);
        }

        List<DomainFlowConfigurationList.DomainItem> domains = Optional.ofNullable(configuration.getDomains()).orElse(Collections.emptyList())
                .stream()
                .filter(d -> !registrationProfileRepository.existsByDomainId(d.getDomainId()))
                .collect(Collectors.toList());
        if (!domains.isEmpty()) {
            initializeDomains(domains);
        }
    }

    private void initializeFieldTypes(List<DomainFlowConfigurationList.FieldTypeItem> fieldTypes) {
        List<FieldType> appFieldTypes = fieldTypes.stream()
                .map(DomainFlowConfigurationList.FieldTypeItem::toEntity)
                .collect(Collectors.toList());
        fieldTypeRepository.saveAll(appFieldTypes);
    }

    private void initializeDomains(List<DomainFlowConfigurationList.DomainItem> domainItems) {
        domainItems.forEach(this::initializeDomain);
    }

    private void initializeDomain(DomainFlowConfigurationList.DomainItem domainItem) {
        List<DomainFlowConfigurationList.RegistrationProfileItem> profileItems = Optional.ofNullable(domainItem.getRegistrationProfiles())
                .orElse(Collections.emptyList());
        if (!profileItems.isEmpty()) {
            registerProfiles(domainItem.getDomainId(), profileItems);
        }
        List<DomainFlowConfigurationList.FieldItem> fieldItems = Optional.ofNullable(domainItem.getFields())
                .orElse(Collections.emptyList());
        if (!fieldItems.isEmpty()) {
            registerFields(domainItem.getDomainId(), fieldItems);
        }
    }

    private void registerProfiles(String domainId, List<DomainFlowConfigurationList.RegistrationProfileItem> profileItems) {
        List<RegistrationProfile> profiles = profileItems
                .stream()
                .map(item -> item.toEntity(domainId))
                .collect(Collectors.toList());
        profiles = registrationProfileRepository.saveAll(profiles);
        profiles.stream()
                .map(p -> new RegistrationFieldModifiedEvent(p.getId()))
                .forEach(publisher::publishEvent);
    }

    private void registerFields(String domainId, List<DomainFlowConfigurationList.FieldItem> fieldItems) {
        List<Field> fields = fieldItems.stream()
                .map(f -> f.toEntity(domainId))
                .collect(Collectors.toList());
        fieldRepository.saveAll(fields);
    }
}
