package com.mmadu.registration.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@EqualsAndHashCode
@ToString
public class UserForm {
    private Map<String, Object> properties = new HashMap<>();

    public void set(String property, Object object) {
        properties.put(property, object);
    }

    public Optional<Object> get(String property){
        return Optional.ofNullable(properties.get(property));
    }
}