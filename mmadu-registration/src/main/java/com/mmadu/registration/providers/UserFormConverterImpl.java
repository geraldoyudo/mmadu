package com.mmadu.registration.providers;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.models.UserForm;
import com.mmadu.registration.models.UserModel;
import com.mmadu.registration.typeconverters.FieldConverterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.mmadu.registration.utils.RegistrationRequestFields.FIELD_ID_TYPE_MAP;
import static com.mmadu.registration.utils.RegistrationRequestFields.FIELD_LIST;

@Component
public class UserFormConverterImpl implements UserFormConverter {
    private HttpServletRequest request;
    private FieldConverterFactory fieldConverterFactory;

    public UserFormConverterImpl() throws Exception {
        this.request = request;
    }

    @Autowired
    public void setFieldConverterFactory(FieldConverterFactory fieldConverterFactory) {
        this.fieldConverterFactory = fieldConverterFactory;
    }

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public UserModel convertToUserProperties(String domainId, UserForm userForm) {
        Map<String, FieldType> propertyTypeMap = getPropertyTypeMap();
        Map<String, Object> returnValue = new HashMap<>();
        propertyTypeMap.forEach((property, type) -> {
            userForm.get(property).ifPresent(existingPropertyValue -> {
                returnValue.put(property, fieldConverterFactory.getConverterForType(type)
                        .convertToObject(existingPropertyValue));
            });

        });
        return new UserModel(returnValue);
    }

    private Map<String, FieldType> getPropertyTypeMap() {
        Map<String, FieldType> propertyTypeMap = new HashMap<>();
        List<Field> fields = Optional.ofNullable((List<Field>) request.getAttribute(FIELD_LIST))
                .orElse(Collections.emptyList());
        Map<String, FieldType> fieldTypeMap =
                Optional.ofNullable((Map<String, FieldType>) request.getAttribute(FIELD_ID_TYPE_MAP))
                        .orElse(Collections.emptyMap());
        fields
                .stream()
                .forEach(field -> {
                    FieldType type = fieldTypeMap.get(field.getFieldTypeId());
                    propertyTypeMap.put(field.getProperty(), type);
                });
        return propertyTypeMap;
    }
}
