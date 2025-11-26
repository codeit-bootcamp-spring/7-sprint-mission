package com.sprint.mission.discodeit.dto.request.channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.UUID;

import static com.sprint.mission.discodeit.service.util.StaticString.*;


public record ChannelPrivateCreateRequestDto(

        @NotNull(message = "ChannelPrivate participantIds")
        HashSet<UUID> participantIds,
        String name ,
        String description ,
        boolean isTextChannel

) {
    public ChannelPrivateCreateRequestDto(HashSet<UUID> participantIds) {
        this(participantIds,DEFAULT_CHANNEL_NAME,DEFAULT_CHANNEL_DESCRIPTION,true);
    }


}
