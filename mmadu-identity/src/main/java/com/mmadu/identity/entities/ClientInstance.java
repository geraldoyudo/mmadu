package com.mmadu.identity.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class ClientInstance {
    @Id
    private String id;
    private String clientId;
    private ClientType clientType;
    private String clientProfile;
    private ClientCredentials credentials;
    private String identifier;
    private List<String> redirectionUris;
    private List<String> allowedHosts;
    private boolean tlsEnabled;
    private List<String> supportedGrantTypes;

}
