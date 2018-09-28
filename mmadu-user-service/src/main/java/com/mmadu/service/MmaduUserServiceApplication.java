package com.mmadu.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class MmaduUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MmaduUserServiceApplication.class, args);
	}
}
