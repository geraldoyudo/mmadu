package com.mmadu.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequest {
    private List<UserPatch> updates = new LinkedList<>();

    public void addUpdate(UserPatch patch) {
        this.updates.add(patch);
    }
}
