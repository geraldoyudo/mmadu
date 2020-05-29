package com.mmadu.identity.config;

import com.mmadu.identity.entities.ClientSecretCredentials;
import com.mmadu.identity.utils.ClientCredentialsRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientCredentialsConfig {

    @Bean
    public ClientCredentialsRegistration clientSecret(){
        return new ClientCredentialsRegistration("secret", ClientSecretCredentials.class);
    }
}
