package com.sprint.mission.discodeit.mapper.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;


public record PrivateChannelCreateRequest( //all private final
                                           @NotNull                                //@NotBlank(message = "Channel name is mandatory")
        List<UUID> participantIds // PRIVATE 일 경우 사용
) {
    public static PrivateChannelCreateRequest from(List<UUID> userIDs) {
        return new PrivateChannelCreateRequest(userIDs);
    }
}
