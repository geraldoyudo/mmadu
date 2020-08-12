package com.mmadu.otp.service.config;

import com.mmadu.security.api.MmaduWebSecurityConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class WebSecurityConfig extends MmaduWebSecurityConfigurer {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/repo/domainOtpConfigurations/**")
                .hasAuthority("otp_config.read")
                .antMatchers(HttpMethod.GET, "/repo/otpCounters/**")
                .hasAuthority("otp_counter.read")
                .antMatchers(HttpMethod.GET, "/repo/otpProfiles/**")
                .hasAuthority("otp_profile.read")
                .antMatchers(HttpMethod.GET, "/repo/otpTokens/**")
                .hasAuthority("otp_token.read")
                .anyRequest()
                .authenticated();
    }
}
