package com.mmadu.service.models;

public class NewGroupUserRequest {
    private String id;
    private String group;

    public NewGroupUserRequest() {
    }

    public NewGroupUserRequest(String id, String group) {
        this.id = id;
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
