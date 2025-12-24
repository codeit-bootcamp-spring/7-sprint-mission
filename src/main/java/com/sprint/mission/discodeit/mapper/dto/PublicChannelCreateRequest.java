package com.sprint.mission.discodeit.mapper.dto;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelCreateRequest( //all private final
                                          @NotBlank
    String name,
                                          @NotBlank
    String description
) {
    public static PublicChannelCreateRequest from(String channelName, String description) {
        return new PublicChannelCreateRequest(
            channelName,
            description
        );
    }
}
