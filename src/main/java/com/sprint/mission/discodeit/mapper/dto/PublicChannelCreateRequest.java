package com.sprint.mission.discodeit.mapper.dto;

import jakarta.validation.constraints.NotNull;

public record PublicChannelCreateRequest( //all private final
                                          @NotNull
    String name,
                                          @NotNull
    String description
) {
    public static PublicChannelCreateRequest from(String channelName, String description) {
        return new PublicChannelCreateRequest(
            channelName,
            description
        );
    }
}
