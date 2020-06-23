package com.mmadu.identity.populators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.identity.config.DomainIdentityConfigurationList;
import com.mmadu.identity.entities.*;
import com.mmadu.identity.providers.client.instance.ClientInstanceCredentialsHasher;
import com.mmadu.identity.repositories.*;
import com.mmadu.identity.services.security.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DomainIdentityPopulator {
    public static final String CREDENTIAL_ID = "credentialId";
    private DomainIdentityConfigurationList domainIdentityConfigurationList;
    private DomainIdentityConfigurationRepository domainIdentityConfigurationRepository;
    private ApplicationEventPublisher publisher;
    private ClientRepository clientRepository;
    private ClientInstanceRepository clientInstanceRepository;
    private ResourceRepository resourceRepository;
    private ScopeRepository scopeRepository;
    private ObjectMapper objectMapper;
    private CredentialService credentialService;
    private DomainIdentityConfigurationList.CredentialConverter credentialConverter;
    private ClientInstanceCredentialsHasher clientInstanceCredentialsHasher;

    @Autowired
    public void setDomainIdentityConfigurationRepository(DomainIdentityConfigurationRepository domainIdentityConfigurationRepository) {
        this.domainIdentityConfigurationRepository = domainIdentityConfigurationRepository;
    }

    @Autowired
    public void setDomainIdentityConfigurationList(DomainIdentityConfigurationList domainIdentityConfigurationList) {
        this.domainIdentityConfigurationList = domainIdentityConfigurationList;
    }

    @Autowired
    public void setPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Autowired
    public void setScopeRepository(ScopeRepository scopeRepository) {
        this.scopeRepository = scopeRepository;
    }

    @Autowired
    public void setClientInstanceRepository(ClientInstanceRepository clientInstanceRepository) {
        this.clientInstanceRepository = clientInstanceRepository;
    }

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Autowired
    public void setResourceRepository(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @Autowired
    public void setClientInstanceCredentialsHasher(ClientInstanceCredentialsHasher clientInstanceCredentialsHasher) {
        this.clientInstanceCredentialsHasher = clientInstanceCredentialsHasher;
    }

    @PostConstruct
    public void init() {
        credentialConverter = credentials -> objectMapper.convertValue(credentials, ClientCredentials.class);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void setUpDomainIdentities() {
        List<DomainIdentityConfigurationList.DomainIdentityItem> unInitializedDomains =
                Optional.ofNullable(domainIdentityConfigurationList.getDomains())
                        .orElse(Collections.emptyList())
                        .stream()
                        .filter(domainIdentityItem -> !domainIdentityConfigurationRepository.existsByDomainId(domainIdentityItem.getDomainId()))
                        .collect(Collectors.toList());
        if (!unInitializedDomains.isEmpty()) {
            initializeDomains(unInitializedDomains);
        }
    }

    public void initializeDomains(List<DomainIdentityConfigurationList.DomainIdentityItem> domainIdentityItems) {
        List<String> domainIds = new LinkedList<>();
        for (DomainIdentityConfigurationList.DomainIdentityItem item : domainIdentityItems) {
            domainIds.add(initializeDomain(item).getDomainId());
        }
        publisher.publishEvent(new DomainIdentityPopulatedEvent(domainIds));
    }

    private DomainIdentityConfiguration initializeDomain(DomainIdentityConfigurationList.DomainIdentityItem item) {
        DomainIdentityConfiguration configuration = domainIdentityConfigurationRepository.save(processCredentials(item.toEntity()));
        DomainContext context = new DomainContext(configuration);
        initializeDomainClients(item.getClients(), context);
        initializeDomainClientInstances(item.getClientInstances(), context);
        initializeDomainResources(item.getResources(), context);
        initializeDomainScopes(item.getScopes(), context);
        return configuration;
    }

    private DomainIdentityConfiguration processCredentials(DomainIdentityConfiguration configuration) {
        String domainId = configuration.getDomainId();
        configuration.setAccessTokenProperties(addCredential(configuration.getAccessTokenProperties(), domainId));
        configuration.setAuthorizationCodeTypeProperties(addCredential(configuration.getAuthorizationCodeTypeProperties(), domainId));
        configuration.setRefreshTokenProperties(addCredential(configuration.getRefreshTokenProperties(), domainId));
        return configuration;
    }

    private Map<String, Object> addCredential(Map<String, Object> properties, String domainId) {
        if (properties.containsKey(CREDENTIAL_ID)) {
            DomainIdentityConfigurationList.CredentialItem item = objectMapper.convertValue(
                    properties.get(CREDENTIAL_ID),
                    DomainIdentityConfigurationList.CredentialItem.class
            );
            properties.put(CREDENTIAL_ID, credentialService.generateCredentialForDomain(
                    domainId, item.toRequest()
            ));
        }
        return properties;
    }

    private void initializeDomainClients(List<DomainIdentityConfigurationList.ClientItem> clients, DomainContext context) {
        List<Client> appClients = clients
                .stream()
                .map(c -> c.toEntity(context.getDomainId()))
                .collect(Collectors.toList());
        context.addClients(clientRepository.saveAll(appClients));
    }

    private void initializeDomainClientInstances(List<DomainIdentityConfigurationList.ClientInstanceItem> clients, DomainContext context) {
        List<ClientInstance> appClientInstances = clients.stream()
                .map(ci -> ci.toEntity(context.getDomainId(), context, credentialConverter))
                .collect(Collectors.toList());
        appClientInstances.forEach(clientInstanceCredentialsHasher::hashCredentialsBeforeCreate);
        clientInstanceRepository.saveAll(appClientInstances);
    }

    private void initializeDomainResources(List<DomainIdentityConfigurationList.ResourceItem> resources, DomainContext context) {
        List<Resource> appResources = resources.stream()
                .map(r -> r.toEntity(context.getDomainId()))
                .collect(Collectors.toList());
        resourceRepository.saveAll(appResources);
    }

    private void initializeDomainScopes(List<DomainIdentityConfigurationList.ScopeItem> scopes, DomainContext context) {
        List<Scope> appScopes = scopes.stream()
                .map(s -> s.toEntity(context.getDomainId()))
                .collect(Collectors.toList());
        scopeRepository.saveAll(appScopes);
    }

    private static class DomainContext implements DomainIdentityConfigurationList.ClientResolver {
        private final DomainIdentityConfiguration configuration;
        private final Map<String, Client> codeToClientMap = new HashMap<>();

        public DomainContext(DomainIdentityConfiguration configuration) {
            this.configuration = configuration;
        }

        public String getDomainId() {
            return configuration.getDomainId();
        }

        public void addClients(List<Client> clients) {
            clients.forEach(client -> codeToClientMap.put(client.getCode(), client));
        }

        @Override
        public Optional<Client> getClient(String code) {
            return Optional.ofNullable(codeToClientMap.get(code));
        }
    }
}
