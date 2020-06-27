package com.mmadu.registration;

import com.mmadu.registration.config.DomainFlowConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication

@EnableConfigurationProperties({
        DomainFlowConfiguration.class,
})
@EnableAspectJAutoProxy
public class MmaduRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MmaduRegistrationApplication.class, args);
    }

}
