package com.mmadu.service.models;

import lombok.Data;

import java.util.List;

@Data
public class UserUpdateRequest {
    private String query;
    private List<UserPatch> updates;
}
