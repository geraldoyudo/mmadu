package com.mmadu.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Deprecated
public interface MmaduSecurityConfigurer {

    void configure(HttpSecurity http) throws Exception;
}
