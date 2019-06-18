package com.mmadu.registration.populators;

import com.mmadu.registration.config.FieldTypeConfigurationList;
import com.mmadu.registration.repositories.FieldTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ConfigurationFieldTypePopulator {
    @Autowired
    private FieldTypeRepository fieldTypeRepository;
    @Autowired
    private FieldTypeConfigurationList fieldTypeConfigurationList;

    @PostConstruct
    public void populate() {
        if (fieldTypeRepository.count() > 0) {
            return;
        }
        fieldTypeRepository.saveAll(fieldTypeConfigurationList.getFieldTypes());
    }
}
