package com.mmadu.registration.handlers;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.models.RegistrationFieldModifiedEvent;
import com.mmadu.registration.repositories.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class RegistrationFieldsFormUpdater {
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private FieldRepository fieldRepository;

    @HandleAfterSave
    public void handleFieldUpdate(Field field) {
        publisher.publishEvent(new RegistrationFieldModifiedEvent(field.getDomainId()));
    }

    @HandleAfterSave
    public void handleFieldTypeUpdate(FieldType fieldType) {
        fieldRepository.findByFieldTypeId(fieldType.getId())
                .stream()
                .map(field -> field.getDomainId())
                .distinct()
                .forEach(domainId -> publisher.publishEvent(new RegistrationFieldModifiedEvent(domainId)));
    }
}
