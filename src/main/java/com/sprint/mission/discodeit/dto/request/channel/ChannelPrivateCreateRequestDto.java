package com.sprint.mission.discodeit.dto.request.channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.UUID;

import static com.sprint.mission.discodeit.service.util.StaticString.*;


public record ChannelPrivateCreateRequestDto(

        @NotEmpty(message = "사설 채널 생성시 유저 목록은 필수값입니다")
        HashSet<UUID> participantIds,
        String name ,
        String description ,
        boolean isTextChannel

) {
    public ChannelPrivateCreateRequestDto(HashSet<UUID> participantIds) {
        this(new HashSet<>(participantIds),DEFAULT_CHANNEL_NAME,DEFAULT_CHANNEL_DESCRIPTION,true);
    }


}
