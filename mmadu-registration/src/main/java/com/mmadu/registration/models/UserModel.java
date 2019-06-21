package com.mmadu.registration.models;

import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    private Map<String, Object> properties = new HashMap<>();

    public void set(String property, Object value) {
        properties.put(property, value);
    }

    public Optional<Object> get(String property) {
        return Optional.ofNullable(properties.get(property));
    }
}