package com.mmadu.registration.handlers;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.models.RegistrationFieldModifiedEvent;
import com.mmadu.registration.repositories.FieldRepository;
import com.mmadu.registration.repositories.RegistrationProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
@RepositoryEventHandler
public class RegistrationFieldsFormUpdater {
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private RegistrationProfileRepository registrationProfileRepository;

    @HandleAfterSave
    public void handleFieldUpdate(Field field) {
        List<RegistrationProfile> profileIds = registrationProfileRepository.getProfilesLinkedToField(field.getDomainId(), field.getCode());
        profileIds.stream()
                .map(RegistrationProfile::getId)
                .map(RegistrationFieldModifiedEvent::new)
                .forEach(publisher::publishEvent);
    }

    @HandleAfterSave
    public void handleFieldTypeUpdate(FieldType fieldType) {
        List<Field> fields = fieldRepository.findByFieldTypeId(fieldType.getId());
        Map<String, List<String>> domainFieldCodes = new HashMap<>();
        for (Field field : fields) {
            List<String> codes = domainFieldCodes.getOrDefault(field.getDomainId(), new LinkedList<>());
            codes.add(field.getCode());
            domainFieldCodes.put(field.getDomainId(), codes);
        }
        domainFieldCodes.forEach(this::updateDomainRegistrationProfilesLinkedToFields);

    }

    private void updateDomainRegistrationProfilesLinkedToFields(String domainId, List<String> fieldCodes){
        registrationProfileRepository.getProfilesLinkedToFields(domainId, fieldCodes)
                .stream()
                .map(RegistrationProfile::getId)
                .map(RegistrationFieldModifiedEvent::new)
                .forEach(publisher::publishEvent);
    }
}
