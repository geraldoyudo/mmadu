package com.mmadu.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserPatch {
    private PatchOperation operation;
    private String property;
    private Object value;
}
