package com.mmadu.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface MmaduSecurityConfigurer {

    void configure(HttpSecurity http) throws Exception;
}
