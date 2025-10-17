package com.sprint.mission.discodeit.deletedCash;

import java.io.Serializable;

public class DeletedUser implements Serializable {

    private final String name;
    private final Long DeletedTime;

    public DeletedUser(String name) {
        this.name = name;
        this.DeletedTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "DeletedUser{" +
                "name='" + name + '\'' +
                ", DeletedTime=" + DeletedTime +
                '}';
    }

    public String getName() {
        return name;
    }

    public Long getDeletedTime() {
        return DeletedTime;
    }
}
