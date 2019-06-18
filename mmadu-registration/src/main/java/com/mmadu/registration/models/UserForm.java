package com.mmadu.registration.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode
@ToString
public class UserForm {
    private Map<String, Object> properties = new HashMap<>();
}