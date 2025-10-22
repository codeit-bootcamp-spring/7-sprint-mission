package com.sprint.mission.discodeit.dto;

public class DeletedChannelDto {
    private final String name;
    private final Long DeletedTime;

    public DeletedChannelDto(String name) {
        this.name = name;
        this.DeletedTime = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "DeletedChannelDto{" +
                "name='" + name + '\'' +
                ", DeletedTime=" + DeletedTime +
                '}';
    }

    public Long getDeletedTime() {
        return DeletedTime;
    }
}
