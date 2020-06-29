package com.mmadu.identity.validators.client;

import com.mmadu.identity.entities.ClientInstance;
import com.mmadu.identity.repositories.ClientRepository;
import com.mmadu.identity.validators.client.instancestrategies.ClientInstanceValidatingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class ClientInstanceValidator implements Validator {
    private ClientRepository clientRepository;
    private List<ClientInstanceValidatingStrategy> strategies = Collections.emptyList();

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Autowired(required = false)
    public void setStrategies(List<ClientInstanceValidatingStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ClientInstance.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        log.trace("Validating client instance {}", o);
        ClientInstance instance = (ClientInstance) o;
        if (instance.getClientId() != null && !clientRepository.existsById(instance.getClientId())) {
            errors.rejectValue("clientId", "client.not.exists", "client does not exist");
        }
        validateWithStrategies(instance, errors);
    }

    private void validateWithStrategies(ClientInstance instance, Errors errors) {
        strategies.stream()
                .filter(s -> s.apply(instance))
                .forEach(s -> s.validate(instance, errors));
    }
}
