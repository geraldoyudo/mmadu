package com.mmadu.registration.populators;

import com.mmadu.registration.config.FieldsConfigurationList;
import com.mmadu.registration.repositories.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ConfigurationFieldPopulator implements Populator {
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private FieldsConfigurationList fieldsConfigurationList;

    @Override
    @PostConstruct
    public void populate() {
        if (fieldRepository.count() == 0) {
            fieldRepository.saveAll(fieldsConfigurationList.getFields());
        }
    }
}
