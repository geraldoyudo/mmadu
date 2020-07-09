package com.mmadu.registration.security;

import com.mmadu.registration.entities.DomainFlowConfiguration;
import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.entities.RegistrationProfile;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class RepositoryRestSecurity {

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('flow_config.create')")
    public void createDomainFlowConfiguration(@P("domainFlowConfiguration") DomainFlowConfiguration domainFlowConfiguration) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('flow_config.update')")
    public void saveDomainFlowConfiguration(@P("domainFlowConfiguration") DomainFlowConfiguration domainFlowConfiguration) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('flow_config.delete')")
    public void deleteDomainFlowConfiguration(@P("domainFlowConfiguration") DomainFlowConfiguration domainFlowConfiguration) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('field.create')")
    public void createField(@P("field") Field field) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('field.update')")
    public void saveField(@P("field") Field field) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('field.delete')")
    public void deleteField(@P("field") Field field) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('field_type.create')")
    public void createFieldType(@P("fieldType") FieldType fieldType) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('field_type.update')")
    public void saveFieldType(@P("fieldType") FieldType fieldType) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('field_type.delete')")
    public void deleteFieldType(@P("fieldType") FieldType fieldType) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('reg_profile.create')")
    public void createRegistrationProfile(@P("registrationProfile") RegistrationProfile registrationProfile) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('reg_profile.update')")
    public void saveRegistrationProfile(@P("registrationProfile") RegistrationProfile registrationProfile) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('reg_profile.delete')")
    public void deleteRegistrationProfile(@P("registrationProfile") RegistrationProfile registrationProfile) {

    }


}
