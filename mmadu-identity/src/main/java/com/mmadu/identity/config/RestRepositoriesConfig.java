package com.mmadu.identity.config;

import com.mmadu.identity.entities.*;
import com.mmadu.identity.validators.client.ClientInstanceValidator;
import com.mmadu.identity.validators.client.ClientValidator;
import com.mmadu.identity.validators.domain.DomainIdentityConfigurationValidator;
import com.mmadu.identity.validators.domain.HasDomainValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.validation.Validator;

@Configuration
public class RestRepositoriesConfig implements RepositoryRestConfigurer {
    public static final String BEFORE_CREATE = "beforeCreate";
    public static final String BEFORE_SAVE = "beforeSave";

    @Autowired
    private ClientValidator clientValidator;

    @Autowired
    @Qualifier("web")
    private Validator webValidator;

    @Autowired
    private ClientInstanceValidator clientInstanceValidator;
    @Autowired
    private HasDomainValidator hasDomainValidator;
    @Autowired
    private DomainIdentityConfigurationValidator domainIdentityConfigurationValidator;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(
                Client.class, ClientInstance.class, DomainIdentityConfiguration.class, Resource.class, Scope.class
        );
    }

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        validatingListener.addValidator(BEFORE_CREATE, webValidator);
        validatingListener.addValidator(BEFORE_SAVE, webValidator);
        validatingListener.addValidator(BEFORE_CREATE, hasDomainValidator);
        validatingListener.addValidator(BEFORE_SAVE, hasDomainValidator);
        validatingListener.addValidator(BEFORE_CREATE, clientValidator);
        validatingListener.addValidator(BEFORE_SAVE, clientValidator);
        validatingListener.addValidator(BEFORE_CREATE, clientInstanceValidator);
        validatingListener.addValidator(BEFORE_SAVE, clientInstanceValidator);
        validatingListener.addValidator(BEFORE_CREATE, domainIdentityConfigurationValidator);
        validatingListener.addValidator(BEFORE_SAVE, domainIdentityConfigurationValidator);
    }
}
