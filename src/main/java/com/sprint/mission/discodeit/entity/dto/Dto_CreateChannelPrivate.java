package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;


public record Dto_CreateChannelPrivate( //all private final
                                        //@NotBlank(message = "Channel name is mandatory")
        List<UUID> participantIds // PRIVATE 일 경우 사용
) {
    public static Dto_CreateChannelPrivate from(List<UUID> userIDs) {
        return new Dto_CreateChannelPrivate(userIDs);
    }
}
