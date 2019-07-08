package com.mmadu.registration.config;

import com.mmadu.registration.entities.FieldType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "mmadu.registration.field-type-config")
@Data
public class FieldTypeConfigurationList {
    private List<FieldType> fieldTypes = new LinkedList<>();
}
