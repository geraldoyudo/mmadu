package com.mmadu.registration.config;

import com.mmadu.registration.models.themes.ThemeConfiguration;
import com.mmadu.security.api.MmaduWebSecurityConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableConfigurationProperties(ThemeConfiguration.class)
public class WebSecurityConfig extends MmaduWebSecurityConfigurer {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/*/register/**", "/themes/**", "/js/**",  "/webjars/**", "/css/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/repo/fields/**")
                .hasAuthority("field.read")
                .antMatchers(HttpMethod.GET, "/repo/fieldTypes/**")
                .hasAuthority("field_type.read")
                .antMatchers(HttpMethod.GET, "/repo/registrationProfiles/**")
                .hasAuthority("reg_profile.read")
                .antMatchers(HttpMethod.GET, "/repo/domainFlowConfigurations/**")
                .hasAuthority("flow_config.read")
                .anyRequest()
                .authenticated();
    }
}
