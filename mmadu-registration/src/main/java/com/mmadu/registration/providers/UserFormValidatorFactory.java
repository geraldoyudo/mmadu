package com.mmadu.registration.providers;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.repositories.FieldRepository;
import com.mmadu.registration.repositories.FieldTypeRepository;
import com.mmadu.registration.services.RegistrationProfileService;
import com.mmadu.registration.typeconverters.FieldValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mmadu.registration.utils.RegistrationRequestFields.*;

@Component
public class UserFormValidatorFactory {
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private FieldTypeRepository fieldTypeRepository;
    @Autowired
    private FieldValidatorFactory fieldValidatorFactory;
    @Autowired
    private RegistrationProfileService registrationProfileService;
    @Autowired
    private HttpServletRequest request;

    public UserFormValidator createValidatorForDomainAndCode(String domainId, String profileCode) {
        RegistrationProfile profile = registrationProfileService.getProfileForDomainAndCode(domainId, profileCode);
        List<Field> fieldList = fieldRepository.findByDomainIdAndCodeIn(domainId, profile.getFields());
        List<String> fieldTypeIds = fieldList.stream()
                .map(Field::getFieldTypeId)
                .distinct()
                .collect(Collectors.toList());
        Iterable<FieldType> fieldTypes = fieldTypeRepository.findAllById(fieldTypeIds);
        Map<String, FieldType> fieldTypeMap = new HashMap<>();
        StreamSupport.stream(fieldTypes.spliterator(), false)
                .forEach(fieldType -> fieldTypeMap.put(fieldType.getId(), fieldType));
        if (request != null) {
            request.setAttribute(FIELD_TYPE_LIST, fieldTypes);
            request.setAttribute(FIELD_LIST, fieldList);
            request.setAttribute(FIELD_ID_TYPE_MAP, fieldTypeMap);
        }
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
