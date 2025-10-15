package com.sprint.mission.discodeit.dto;

public class DeletedUserDto {
    private final String name;
    private final Long DeletedTime;

    public DeletedUserDto(String name) {
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
