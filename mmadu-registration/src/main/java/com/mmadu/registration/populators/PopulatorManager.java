package com.mmadu.registration.populators;

import com.mmadu.registration.providers.DomainRegistrationFormFieldsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class PopulatorManager {
    @Autowired
    private List<Populator> populators;
    @Autowired
    private DomainRegistrationFormFieldsManager domainRegistrationFormFieldsManager;

    @PostConstruct
    public void initializeFields() throws Exception {
        domainRegistrationFormFieldsManager.startMonitoring();
    }
}
