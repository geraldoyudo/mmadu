package com.mmadu.registration.providers;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.repositories.FieldRepository;
import com.mmadu.registration.repositories.FieldTypeRepository;
import com.mmadu.registration.typeconverters.FieldValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class UserFormValidatorFactory {
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private FieldTypeRepository fieldTypeRepository;
    @Autowired
    private FieldValidatorFactory fieldValidatorFactory;

    public UserFormValidator createValidatorForDomain(String domainId) {
        List<Field> fieldList = fieldRepository.findByDomainId(domainId);
        List<String> fieldTypeIds = fieldList.stream()
                .map(field -> field.getFieldTypeId())
                .distinct()
                .collect(Collectors.toList());
        Iterable<FieldType> fieldTypes = fieldTypeRepository.findAllById(fieldTypeIds);
        Map<String, FieldType> fieldTypeMap = new HashMap<>();
        StreamSupport.stream(fieldTypes.spliterator(), false)
                .forEach(fieldType -> fieldTypeMap.put(fieldType.getId(), fieldType));

        return new UserFormValidator(domainId,
                fieldList.stream()
                        .map(field ->
                                fieldValidatorFactory.getValidatorForFieldType(
                                        fieldTypeMap.get(field.getFieldTypeId()),
                                        field
                                )
                        )
                        .collect(Collectors.toList())
        );
    }
}
