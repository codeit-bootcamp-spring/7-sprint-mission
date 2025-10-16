package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class DeletedChannel implements Serializable {

    private final String name;
    private final Long DeletedTime;

    public DeletedChannel(String name) {
        this.name = name;
        this.DeletedTime = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "DeletedChannel{" +
                "name='" + name + '\'' +
                ", DeletedTime=" + DeletedTime +
                '}';
    }

    public Long getDeletedTime() {
        return DeletedTime;
    }
}
