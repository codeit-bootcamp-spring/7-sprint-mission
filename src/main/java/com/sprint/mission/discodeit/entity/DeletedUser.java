package com.sprint.mission.discodeit.entity;

public class DeletedUser {

    private final String name;
    private final Long DeletedTime;

    public DeletedUser(String name) {
        this.name = name;
        this.DeletedTime = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public Long getDeletedTime() {
        return DeletedTime;
    }
}
