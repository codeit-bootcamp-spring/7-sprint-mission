package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Entity;

public class ChannelDto extends Entity{

    private final String name;
    private final String description;
    private final boolean isPublic;
    private final boolean isTextChannel;


    public ChannelDto(String name, String description, boolean isPublic, boolean isTextChannel) {
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.isTextChannel = isTextChannel;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isTextChannel() {
        return isTextChannel;
    }
}
