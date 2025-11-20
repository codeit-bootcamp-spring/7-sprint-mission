package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotNull;

public record Dto_CreateChannelPublic( //all private final
    @NotNull
    String name,
    @NotNull
    String description
) {
    public static Dto_CreateChannelPublic from(String channelName, String description) {
        return new Dto_CreateChannelPublic(
            channelName,
            description
        );
    }
}
