package com.mmadu.registration;

import com.mmadu.registration.config.DomainFlowConfiguration;
import com.mmadu.registration.config.FieldTypeConfigurationList;
import com.mmadu.registration.config.FieldsConfigurationList;
import com.mmadu.registration.config.ProfileConfigurationList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication

@EnableConfigurationProperties({
        DomainFlowConfiguration.class,
        FieldsConfigurationList.class,
        FieldTypeConfigurationList.class,
        ProfileConfigurationList.class
})
@EnableAspectJAutoProxy
public class MmaduRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MmaduRegistrationApplication.class, args);
    }

}
