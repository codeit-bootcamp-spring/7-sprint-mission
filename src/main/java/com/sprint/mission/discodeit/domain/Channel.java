package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel {

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private String name;
    private String description;
    private ChannelType type;


    public Channel(String name, String description, boolean isPrivate) {
        this.name = name;
        this.description = description;
        if (isPrivate) {
            this.type = ChannelType.PRIVATE;
        } else {
            this.type = ChannelType.PUBLIC;
        }

    }
}