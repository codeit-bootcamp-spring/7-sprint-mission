package com.sprint.mission.discodeit.entity.dto;

public record Dto_CreateChannelPublic( //all private final
        String name,
        String description
) {
    public static Dto_CreateChannelPublic from(String channelName, String description) {
        return new Dto_CreateChannelPublic(
            channelName,
            description
        );
    }
}
