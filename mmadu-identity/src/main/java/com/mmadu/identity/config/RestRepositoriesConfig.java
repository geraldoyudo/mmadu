package com.mmadu.identity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.mmadu.identity.entities.Client;
import com.mmadu.identity.entities.ClientCredentials;
import com.mmadu.identity.entities.ClientInstance;
import com.mmadu.identity.entities.DomainConfiguration;
import com.mmadu.identity.utils.ClientCredentialsDeserializer;
import com.mmadu.identity.utils.ClientCredentialsSerializer;
import com.mmadu.identity.validators.ClientInstanceValidator;
import com.mmadu.identity.validators.ClientValidator;
import com.mmadu.identity.validators.HasDomainValidator;
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
    private ClientCredentialsDeserializer clientCredentialsDeserializer;
    @Autowired
    private ClientCredentialsSerializer clientCredentialsSerializer;

    @Autowired
    @Qualifier("web")
    private Validator webValidator;

    @Autowired
    private ClientInstanceValidator clientInstanceValidator;
    @Autowired
    private HasDomainValidator hasDomainValidator;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(
                Client.class, ClientInstance.class, DomainConfiguration.class
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
    }

    @Override
    public void configureJacksonObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new SimpleModule("ClientCredentialsSerializerModule") {
            @Override
            public void setupModule(SetupContext context) {
                SimpleSerializers serializers = new SimpleSerializers();
                SimpleDeserializers deserializers = new SimpleDeserializers();
                serializers.addSerializer(ClientCredentials.class, clientCredentialsSerializer);
                deserializers.addDeserializer(ClientCredentials.class, clientCredentialsDeserializer);
                context.addSerializers(serializers);
                context.addDeserializers(deserializers);
            }
        });
    }
}
