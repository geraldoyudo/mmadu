package com.mmadu.registration.config;

import com.mmadu.registration.entities.Field;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "mmadu.registration.field-config")
@Data
public class FieldsConfigurationList {
    private List<Field> fields = new LinkedList<>();
}
