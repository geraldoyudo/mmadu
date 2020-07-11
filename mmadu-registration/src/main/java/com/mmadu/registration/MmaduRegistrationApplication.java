package com.mmadu.registration;

import com.mmadu.registration.config.DomainFlowConfigurationList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableConfigurationProperties({
        DomainFlowConfigurationList.class,
})
@EnableAspectJAutoProxy
@EnableCaching
public class MmaduRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MmaduRegistrationApplication.class, args);
    }

}
