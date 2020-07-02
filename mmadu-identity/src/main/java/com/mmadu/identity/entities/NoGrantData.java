package com.mmadu.identity.entities;

import lombok.Data;

@Data
public class NoGrantData implements GrantData {

    @Override
    public String getType() {
        return "none";
    }

    public static NoGrantData noGrantData() {
        return new NoGrantData();
    }
}
