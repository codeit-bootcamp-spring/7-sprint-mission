package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
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
//    private final List<String> members;

    public Channel(String name, String description, boolean isPrivate, List<String> members) {
        this.name = name;
        this.description = description;
        if (isPrivate) {
            this.type = ChannelType.PRIVATE;
        } else {
            this.type = ChannelType.PUBLIC;
        }
//        if (members == null) {
//            this.members = new ArrayList<>();
//        } else {
//            this.members = members;
//        }
//    }

    }
}